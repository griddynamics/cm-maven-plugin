package com.griddynamics.maven.plugin.cm;

import org.apache.maven.plugin.MojoExecutionException;

public class MergeException extends MojoExecutionException {

    public MergeException(String message) {
        super(message);
    }

}
