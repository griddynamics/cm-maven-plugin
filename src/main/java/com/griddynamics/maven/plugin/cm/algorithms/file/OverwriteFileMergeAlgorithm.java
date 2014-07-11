package com.griddynamics.maven.plugin.cm.algorithms.file;

import com.griddynamics.maven.plugin.cm.FileMergeContext;
import com.griddynamics.maven.plugin.cm.MergeException;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;

public class OverwriteFileMergeAlgorithm
        extends AbstractConfigurableFileMergeAlgorithm {

    @Override
    protected boolean canMerge(File source, File target) {
        return source.isFile() && target.isFile();
    }

    @Override
    public void merge(FileMergeContext mergeContext) throws IOException, MergeException {
        FileUtils.copyFile(mergeContext.getSourceFile(), mergeContext.getTargetFile());
    }

}
