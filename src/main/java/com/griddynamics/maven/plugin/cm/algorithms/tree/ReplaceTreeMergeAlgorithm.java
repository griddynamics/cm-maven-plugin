package com.griddynamics.maven.plugin.cm.algorithms.tree;

import com.griddynamics.maven.plugin.cm.MergeException;
import com.griddynamics.maven.plugin.cm.TreeMergeContext;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class ReplaceTreeMergeAlgorithm extends AbstractConfigurableTreeMergeAlgorithm {

    @Override
    protected boolean canMerge(File source, File target) {
        return source.exists() && target.exists();
    }

    @Override
    public void merge(TreeMergeContext mergeContext) throws IOException, MergeException {
        FileUtils.deleteDirectory(mergeContext.getTargetDir());
        FileUtils.copyDirectory(mergeContext.getSourceDir(), mergeContext.getTargetDir());
    }

}

