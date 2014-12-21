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
        return source.isDirectory() && target.isDirectory();
    }

    @Override
    public void merge(TreeMergeContext context) throws IOException, MergeException {
        File source = context.getSourceDir();

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
