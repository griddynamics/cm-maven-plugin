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
