package com.griddynamics.maven.plugin.cm.algorithms.tree;

import com.griddynamics.maven.plugin.cm.MergeException;
import com.griddynamics.maven.plugin.cm.TreeMergeContext;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;

public class CopyTreeMergeAlgorithm
        extends AbstractConfigurableTreeMergeAlgorithm {

    @Override
    protected boolean canMerge(File source, File target) {
        return source.isDirectory() && !target.exists();
    }

    @Override
    public void merge(TreeMergeContext mergeContext) throws IOException, MergeException {
        org.apache.commons.io.FileUtils.copyDirectory(mergeContext.getSourceDir(), mergeContext.getTargetDir());
    }

}
