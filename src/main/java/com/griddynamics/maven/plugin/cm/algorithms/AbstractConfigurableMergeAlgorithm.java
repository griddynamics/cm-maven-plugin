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
package com.griddynamics.maven.plugin.cm.algorithms;

import com.griddynamics.maven.plugin.cm.MergeAlgorithm;
import com.griddynamics.maven.plugin.cm.MergeContext;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

public abstract class AbstractConfigurableMergeAlgorithm<T extends MergeContext> implements MergeAlgorithm<T> {

    private static final String UNIVERSAL_SEPARATOR = "/";

    private static final String PROP_INCLUDES = "includes";

    private static final String PROP_EXCLUDES = "excludes";

    private Pattern includes = null;

    private Pattern excludes = null;

    public final void setProperties(Properties properties) {
        includes = calculateRegexList(properties, PROP_INCLUDES);
        excludes = calculateRegexList(properties, PROP_EXCLUDES);
    }

    private Pattern calculateRegexList(Properties properties, String propertyName) {
        if (properties == null) {
            return null;
        }
        String patternString = (String) properties.get(propertyName);
        if (patternString != null) {
            return Pattern.compile(patternString.replace(UNIVERSAL_SEPARATOR, File.separator));
        } else {
            return null;
        }
    }

    protected final boolean checkPatterns(String fileName, String relativePath) {
        if (includes != null && (includes.matcher(fileName).matches() || includes.matcher(relativePath).matches())) {
            return true;
        }
        if (excludes != null && (excludes.matcher(fileName).matches() || excludes.matcher(relativePath).matches())) {
            return false;
        }
        return includes == null || excludes != null;
    }

    protected abstract boolean canMerge(File source, File target);

}
