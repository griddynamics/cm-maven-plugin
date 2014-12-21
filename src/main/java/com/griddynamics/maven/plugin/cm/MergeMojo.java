/*
 * Copyright 2014 Grid Dynamics International, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.griddynamics.maven.plugin.cm;

import com.griddynamics.maven.plugin.cm.algorithms.file.PropertiesFileMergeAlgorithm;
import com.griddynamics.maven.plugin.cm.algorithms.tree.RecursiveTreeMergeAlgorithm;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;
import org.codehaus.plexus.util.StringUtils;

import java.io.*;
import java.util.*;

/**
 * Merges configuration from different configuration layers according to layers and merge algorithms configuration.
 *
 * Plugin builds hierarchical configuration into several artifacts. Number of artifacts depends on configuration and
 * project structure.
 */
@Mojo(name = "merge", defaultPhase = LifecyclePhase.COMPILE)
public class MergeMojo extends AbstractMojo {

    /**
     * Directory in which plugin will perform all its build operations and to which it will put generated artifacts.
     */
    @Parameter(defaultValue = "${project.build.directory}")
    private File buildDir;

    /**
     * Name of directory under which the configuration layer data is located.
     */
    @Parameter(defaultValue = "config")
    private String configDir;

    /**
     * Artifacts archive type.
     */
    @Parameter(defaultValue = "zip")
    private String type;

    /**
     * Definition of configuration layers.
     */
    @Parameter
    private LayerModel[] layers = new LayerModel[] {
            new LayerModel("application", false, false),
            new LayerModel("environmentTypes", false, true),
            new LayerModel("environments", false, true),
            new LayerModel("instances", false, true)
        };

    /**
     * Ordered list of data processors available to manage different types of files.<br/>
     */
    @Parameter
    private DataProcessorModel[] processors = {

        };

    /**
     * Ordered list of data merge algorithms.<br/>
     *
     * The first applicable algorithm from list will be used to merge data in files.
     */
    @Parameter
    private MergeAlgorithmModel[] dataAlgorithms = {

        };

    /**
     * Ordered list of file merge algorithms to use.<br/>
     * First one which supports files merge will be used. If non of specified algorithm supports merge, the merge will
     * fail with {@link com.griddynamics.maven.plugin.cm.MergeException}.
     */
    @Parameter
    private MergeAlgorithmModel[] fileAlgorithms = {
            new MergeAlgorithmModel(PropertiesFileMergeAlgorithm.class.getName(), null)
        };

    /**
     * Ordered list of tree merge algorithms to use.<br/>
     *
     * First one which is able to merge given directories will be used.
     */
    @Parameter
    private MergeAlgorithmModel[] treeAlgorithms = {
            new MergeAlgorithmModel(RecursiveTreeMergeAlgorithm.class.getName(), null)
        };

    private List<DataProcessor> dataProcessors;

    private List<DataMergeAlgorithm> dataMergeAlgorithms;

    private List<FileMergeAlgorithm> fileMergeAlgorithms;

    private List<TreeMergeAlgorithm> treeMergeAlgorithms;

    @Component
    private MavenProjectHelper projectHelper;

    @Component
    private MavenProject mavenProject;

    @Component
    private ArchiverManager archiverManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        prepare();

        merge();
    }

    private void prepare() throws MojoFailureException {
        instantiateProcessors();
        instantiateMergeAlgorithms();
        validateProvidedConfigurationLayers();
    }

    private void validateProvidedConfigurationLayers() throws MojoFailureException {
        for (LayerModel layer : layers) {
            if (layer.getName() == null) {
                throw new MojoFailureException("Name is mandatory attribute for layer");
            }
        }
    }

    private void instantiateProcessors() throws MojoFailureException {
        dataProcessors = new ArrayList<>();
        for (DataProcessorModel dataProcessorModel : processors) {
            try {
                DataProcessor dataProcessor = (DataProcessor) Class.forName(dataProcessorModel.getImplementation()).newInstance();
                dataProcessors.add(dataProcessor);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                throw new MojoFailureException("Provided data processor could not be instantiated", e);
            }
        }
    }

    private void instantiateMergeAlgorithms() throws MojoFailureException {
        dataMergeAlgorithms = instantiateGenericMergeAlgorithms(dataAlgorithms);
        fileMergeAlgorithms = instantiateGenericMergeAlgorithms(fileAlgorithms);
        treeMergeAlgorithms = instantiateGenericMergeAlgorithms(treeAlgorithms);
    }

    private <T extends MergeAlgorithm<?>> List<T> instantiateGenericMergeAlgorithms(MergeAlgorithmModel[] mergeAlgorithmModels) throws MojoFailureException {
        @SuppressWarnings("unchecked")
        List<T> mergeAlgorithms = new ArrayList<>();
        try {
            for (MergeAlgorithmModel mergeAlgorithmModel : mergeAlgorithmModels) {
                @SuppressWarnings("unchecked")
                T mergeAlgorithm = (T) Class.forName(mergeAlgorithmModel.getImplementation()).newInstance();
                mergeAlgorithm.setProperties(mergeAlgorithmModel.getProperties());
                mergeAlgorithms.add(mergeAlgorithm);
            }
        } catch (ClassNotFoundException e) {
            throw new MojoFailureException("Provided merge algorithm not found", e);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new MojoFailureException("Provided merge algorithm could not be instantiated", e);
        } catch (ClassCastException e) {
            throw new MojoFailureException("Provided class does not implement com.griddynamics.maven.plugin.cm.merge.TreeMergeAlgorithm", e);
        }
        return mergeAlgorithms;
    }

    private void merge() throws MojoFailureException {
        try {
            File configurationRoot = new File(mavenProject.getBasedir(), layers[0].getName());
            TraverseContext context = new TraverseContext(new File(configurationRoot, configDir), null);
            if (!(buildDir.isDirectory() || buildDir.mkdir())) {
                throw new IOException("Unable to create build directory " + buildDir);
            }
            merge(context, configurationRoot, 0);
        } catch (IOException | NoSuchArchiverException e) {
            throw new MojoFailureException("Exception occurred during creating artifacts", e);
        } catch (MergeException e) {
            throw new MojoFailureException("Error occurred during file merge", e);
        }
    }

    /**
     * Performs merge according within provided context.
     * @param context the context to perform merge within
     * @param path current path to merge
     * @param level current depth of merge to obtain layer configuration
     * @return 'true' if merge was performed in this call or in recurrent to this method; 'false' otherwise
     * @throws IOException when IO error occurred during merge
     * @throws MergeException when there was error during files merge
     */
    private boolean merge(TraverseContext context, File path, int level) throws IOException, MergeException, NoSuchArchiverException {

        LayerModel layer = layers[level];

        boolean isNode = false;
        int nextLevel = level + 1;
        if (nextLevel < layers.length) {
            File layerTypeDir = new File(path, layers[nextLevel].getName());
            if (layerTypeDir.toString().startsWith(".")) {
                getLog().debug("Ignoring hidden " + layerTypeDir);
            } else if (layerTypeDir.isDirectory()) {
                File[] files = layerTypeDir.listFiles();
                if (files != null && files.length > 0) {
                    for (File layerDir : files) {
                        if (layerDir.isDirectory()) {
                            isNode |= merge(new TraverseContext(context, new File(layerDir, configDir), layerDir.getName()), layerDir, nextLevel);
                        }
                    }
                }
            } else if (layerTypeDir.isFile()) {
                getLog().warn("Ignoring unknown file " + layerTypeDir);
            }
        }

        if ((isNode && layer.includeNode()) || (!isNode && layer.includeLeaf())) {
            mergeContext(context);
            return true;
        }

        return isNode;

    }

    private void mergeContext(TraverseContext context) throws IOException, MergeException, NoSuchArchiverException {
        StringBuilder classifierBuilder = new StringBuilder();
        for (String layerName : context.getLayerNames()) {
            if (layerName != null) {
                classifierBuilder.append('-').append(layerName);
            }
        }
        String classifier = classifierBuilder.delete(0, 1).toString();
        String artifactName;
        if (StringUtils.isEmpty(classifier)) {
            artifactName = mavenProject.getArtifactId();
        } else {
            artifactName = mavenProject.getArtifactId() + "-" + classifier;
        }
        File targetDirectory = new File(buildDir, artifactName);
        if (!targetDirectory.exists()) {
            if (!targetDirectory.mkdir()) {
                throw new IOException("Unable to create target directory " + targetDirectory);
            }
        }
        mergeTrees(context.getDirectories(), targetDirectory);
        File archivedConfigurationFile = archive(targetDirectory, artifactName);
        projectHelper.attachArtifact(mavenProject, type, classifier, archivedConfigurationFile);
    }

    private void mergeTrees(List<File> sourceDirectories, File targetBaseDir) throws IOException, MergeException {
        for (File sourceBaseDir : sourceDirectories) {
            if (sourceBaseDir.isDirectory()) {
                new TreeMergeContext(
                        sourceBaseDir, targetBaseDir,
                        treeMergeAlgorithms, fileMergeAlgorithms, dataMergeAlgorithms, dataProcessors,
                        getLog()
                    ).merge();
            }
        }
    }

    private File archive(File sourceDirectory, String artifactName) throws IOException, NoSuchArchiverException {

        File outputFile = new File(buildDir, artifactName + "." + type);

        Archiver archiver = archiverManager.getArchiver(outputFile);
        archiver.setDestFile(outputFile);
        archiver.addDirectory(sourceDirectory, null, null);
        archiver.createArchive();

        return outputFile;
    }

}
