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
