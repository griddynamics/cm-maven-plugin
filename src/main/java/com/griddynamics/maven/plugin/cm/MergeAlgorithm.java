/*
 * Copyright (c) 2014 Grid Dynamics International, Inc.
 * Classification level: Public
 * Licensed under the Apache License, Version 2.0.
 */
package com.griddynamics.maven.plugin.cm;

import java.io.IOException;
import java.util.Properties;

public interface MergeAlgorithm<T extends MergeContext> {

    boolean canMerge(T mergeContext);

    void setProperties(Properties properties);

    void merge(T mergeContext) throws IOException, MergeException;

}
