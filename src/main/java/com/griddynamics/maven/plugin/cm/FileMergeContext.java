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
package com.griddynamics.maven.plugin.cm;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Set;

public final class FileMergeContext extends MergeContext {

    private final File sourceFile;

    private final File targetFile;

    private final String fileName;

    public FileMergeContext(TreeMergeContext parent, String fileName) {
        super(parent);

        sourceFile = new File(parent.getSourceDir(), fileName);
        if (sourceFile.isDirectory()) {
            throw new IllegalArgumentException("Provided file " + fileName + " is directory in " + parent.getSourceDir());
        }

        targetFile = new File(parent.getTargetDir(), fileName);
        if (targetFile.isDirectory()) {
            throw new IllegalArgumentException("Provided file " + fileName + " is directory in " + parent.getTargetDir());
        }

        this.fileName = fileName;
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public File getTargetFile() {
        return targetFile;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String getPath() {
        return sourceFile.toString().substring(getSourceBaseDir().toString().length());
    }

    @Override
    public void merge() throws IOException, MergeException {
        for (FileMergeAlgorithm fileMergeAlgorithm : getFileMergeAlgorithms()) {
            if (fileMergeAlgorithm.canMerge(this)) {
                if (getLog().isDebugEnabled()) {
                    getLog().debug("Merging " + sourceFile + " to " + targetFile + " using " + fileMergeAlgorithm.getClass().getName());
                }
                fileMergeAlgorithm.merge(this);
                return;
            }
        }
        throw new FileMergeException("Unable to find merge algorithm for " + toString());
    }

    @Override
    public String toString() {
        return fileName;
    }
}
