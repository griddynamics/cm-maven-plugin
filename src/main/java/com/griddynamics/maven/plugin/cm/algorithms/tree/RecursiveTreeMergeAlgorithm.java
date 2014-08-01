/*
 * Copyright (c) 2014 Grid Dynamics International, Inc.
 * Classification level: Public
 * Licensed under the Apache License, Version 2.0.
 */
package com.griddynamics.maven.plugin.cm.algorithms.tree;

import com.griddynamics.maven.plugin.cm.FileMergeContext;
import com.griddynamics.maven.plugin.cm.MergeException;
import com.griddynamics.maven.plugin.cm.TreeMergeAlgorithm;
import com.griddynamics.maven.plugin.cm.TreeMergeContext;
import com.griddynamics.maven.plugin.cm.algorithms.AbstractConfigurableMergeAlgorithm;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class RecursiveTreeMergeAlgorithm
    extends AbstractConfigurableTreeMergeAlgorithm {

    @Override
    public boolean canMerge(File source, File target) {
        return true;
    }

    @Override
    public void merge(TreeMergeContext context) throws IOException, MergeException {
        File source = context.getSourceDir();
        File target = context.getTargetDir();

        if (!target.isDirectory()) {
            if (!target.mkdir()) {
                throw new IOException("Unable to create directory: " + target);
            }
        }

        File[] files = source.listFiles();

        if (files == null) {
            throw new IllegalStateException("Cannot get list of files for directory " + source);
        }

        for (File file : files) {
            String filename = file.getName();
            if (file.isDirectory()) {
                new TreeMergeContext(context, filename).merge();
            } else {
                new FileMergeContext(context, filename).merge();
            }
        }
    }

}
