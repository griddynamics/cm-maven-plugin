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

    private static final String PROP_INCLUDES = "includes";

    private static final String PROP_EXCLUDES = "excludes";

    private final Set<Pattern> includes = new HashSet<>();

    private final Set<Pattern> excludes = new HashSet<>();

    public final void setProperties(Properties properties) {
        includes.addAll(calculateRegexList(properties, PROP_INCLUDES));
        excludes.addAll(calculateRegexList(properties, PROP_EXCLUDES));
    }

    private List<Pattern> calculateRegexList(Properties properties, String propertyName) {
        if (properties == null) {
            return Collections.emptyList();
        }
        String patternsString = (String) properties.get(propertyName);
        if (patternsString != null) {
            List<Pattern> patterns = new ArrayList<>();
            for (String patternString : patternsString.split(File.pathSeparator)) {
                Pattern pattern = Pattern.compile(patternString);
                patterns.add(pattern);
            }
            return patterns;
        } else {
            return Collections.emptyList();
        }
    }

    protected final boolean checkPatterns(String fileName, String relativePath) {
        for (Pattern include : includes) {
            if (include.matcher(fileName).matches() || include.matcher(relativePath).matches()) {
                return true;
            }
        }
        for (Pattern exclude : excludes) {
            if (exclude.matcher(fileName).matches() || exclude.matcher(relativePath).matches()) {
                return false;
            }
        }
        return includes.isEmpty() || !excludes.isEmpty();
    }

    protected abstract boolean canMerge(File source, File target);

}
