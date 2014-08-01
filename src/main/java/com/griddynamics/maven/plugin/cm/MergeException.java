/*
 * Copyright (c) 2014 Grid Dynamics International, Inc.
 * Classification level: Public
 * Licensed under the Apache License, Version 2.0.
 */
package com.griddynamics.maven.plugin.cm;

import org.apache.maven.plugin.MojoExecutionException;

public class MergeException extends MojoExecutionException {

    public MergeException(String message) {
        super(message);
    }

}
