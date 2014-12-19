package com.griddynamics.maven.plugin.cm.algorithms;

import com.griddynamics.maven.plugin.cm.MergeContext;

import java.io.File;
import java.util.Properties;
import java.util.regex.Pattern;

public abstract class AbstractConfigurableFilesystemMergeAlgorithm<T extends MergeContext>
        extends AbstractConfigurableMergeAlgorithm<T> {

    private static final String UNIVERSAL_SEPARATOR = "/";

    private static final String PROP_INCLUDES = "includes";

    private static final String PROP_EXCLUDES = "excludes";

    private Pattern includes = null;

    private Pattern excludes = null;

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

    protected boolean canMerge(File source, File target) {
        return true;
    }

}
