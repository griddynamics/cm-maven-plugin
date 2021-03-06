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
import com.griddynamics.maven.plugin.cm.MergeException;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;

public class CopyFileMergeAlgorithm
        extends AbstractConfigurableFileMergeAlgorithm {

    @Override
    protected boolean canMerge(File source, File target) {
        return source.isFile() && !target.exists() && target.getParentFile().exists();
    }

    @Override
    public void merge(FileMergeContext mergeContext) throws IOException, MergeException {
        FileUtils.copyFile(mergeContext.getSourceFile(), mergeContext.getTargetFile());
    }

}
