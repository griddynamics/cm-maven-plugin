package com.griddynamics.maven.plugin.cm;

import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TreeMergeContext extends MergeContext {

    private final File sourceDir;

    private final File targetDir;

    private final String dirName;

    public TreeMergeContext(File sourceBaseDir, File targetBaseDir, List<FileMergeAlgorithm> fileMergeAlgorithms, List<TreeMergeAlgorithm> treeMergeAlgorithms, Log log) {
        super(sourceBaseDir, targetBaseDir, fileMergeAlgorithms, treeMergeAlgorithms, log);
        this.sourceDir = sourceBaseDir;
        this.targetDir = targetBaseDir;
        this.dirName = ".";
    }

    public TreeMergeContext(TreeMergeContext parent, String dirName) {
        super(parent);
        this.sourceDir = new File(parent.getSourceDir(), dirName);
        if (sourceDir.exists() && !sourceDir.isDirectory()) {
            throw new IllegalArgumentException("Provided source file '" + dirName + "' is not directory in " + parent.getSourceDir());
        }

        this.targetDir = new File(parent.getTargetDir(), dirName);
        if (targetDir.exists() && !targetDir.isDirectory()) {
            throw new IllegalArgumentException("Provided target file '" + dirName + "' is not directory in " + parent.getTargetDir());
        }

        this.dirName = dirName;
    }

    public File getSourceDir() {
        return sourceDir;
    }

    public File getTargetDir() {
        return targetDir;
    }

    public String getFileName() {
        return dirName;
    }

    @Override
    public String getPath() {
        return sourceDir.toString().substring(getSourceBaseDir().toString().length());
    }

    @Override
    public void merge() throws IOException, MergeException {
        for (TreeMergeAlgorithm treeMergeAlgorithm : getTreeMergeAlgorithms()) {
            if (treeMergeAlgorithm.canMerge(this)) {
                treeMergeAlgorithm.merge(this);
                return;
            }
        }
        getLog().info("Skipping directory " + getFileName());
    }

    @Override
    public String toString() {
        return dirName;
    }
}
