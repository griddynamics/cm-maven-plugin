/*
 * Copyright (c) 2014 Grid Dynamics International, Inc.
 * Classification level: Public
 * Licensed under the Apache License, Version 2.0.
 */
package com.griddynamics.maven.plugin.cm;

public class LayerModel {

    private String name;

    private boolean includeNode;

    private boolean includeLeaf;

    public LayerModel() {
        this(null, false, true);
    }

    public LayerModel(String name, boolean includeNode, boolean includeLeaf) {
        this.name = name;
        this.includeNode = includeNode;
        this.includeLeaf = includeLeaf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean includeNode() {
        return includeNode;
    }

    public void setIncludeNode(boolean includeNode) {
        this.includeNode = includeNode;
    }

    public boolean includeLeaf() {
        return includeLeaf;
    }

    public void setIncludeLeaf(boolean includeLeaf) {
        this.includeLeaf = includeLeaf;
    }
}
