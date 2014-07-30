/*
 * Copyright (c) 2014 Grid Dynamics International, Inc.
 * Classification level: Public
 * Licensed under the Apache License, Version 2.0.
 */
package com.griddynamics.maven.plugin.cm.algorithms.file;

import com.griddynamics.maven.plugin.cm.FileMergeContext;
import com.griddynamics.maven.plugin.cm.MergeException;

import java.io.*;

public class JoinFileMergeAlgorithm extends AbstractConfigurableFileMergeAlgorithm {

    @Override
    protected boolean canMerge(File source, File target) {
        return true;
    }

    @Override
    public void merge(FileMergeContext mergeContext) throws IOException, MergeException {
        OutputStream outputStream = new FileOutputStream(mergeContext.getTargetFile(), true);
        InputStream inputStream = new FileInputStream(mergeContext.getSourceFile());
        int len;
        byte[] buf = new byte[1024];
        while ((len = inputStream.read(buf)) > 0) {
            outputStream.write(buf, 0, len);
        }
    }

}
