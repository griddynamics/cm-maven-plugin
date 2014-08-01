/*
 * Copyright (c) 2014 Grid Dynamics International, Inc.
 * Classification level: Public
 * Licensed under the Apache License, Version 2.0.
 */
package com.griddynamics.maven.plugin.cm.algorithms.file;

import com.griddynamics.maven.plugin.cm.FileMergeAlgorithm;
import com.griddynamics.maven.plugin.cm.FileMergeContext;
import org.codehaus.plexus.util.FileUtils;

import java.io.*;
import java.util.Properties;

public class PropertiesFileMergeAlgorithm
    extends AbstractConfigurableFileMergeAlgorithm
    implements FileMergeAlgorithm {

    @Override
    public void merge(FileMergeContext context) throws IOException {
        Properties properties = new SortedProperties();

        File source = context.getSourceFile();
        File target = context.getTargetFile();

        if (!target.exists()) {
            FileUtils.copyFile(source, target);
            return;
        }

        try (
            InputStream sourceInputStream = new FileInputStream(source);
            InputStream targetInputStream = new FileInputStream(target)
        ) {
            properties.load(targetInputStream);
            properties.load(sourceInputStream);
        }
        try (
            OutputStream targetOutputStream = new FileOutputStream(target)
        ) {
            properties.store(targetOutputStream, "Merged");
        }
    }

    @Override
    public boolean canMerge(File source, File target) {
        String sourceName = source.getName();
        return (sourceName.endsWith(".properties") || sourceName.endsWith(".conf")) && sourceName.equals(target.getName());
    }

}
