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
package com.griddynamics.maven.plugin.cm.algorithms.tree;

import com.griddynamics.maven.plugin.cm.TreeMergeAlgorithm;
import com.griddynamics.maven.plugin.cm.TreeMergeContext;
import com.griddynamics.maven.plugin.cm.algorithms.AbstractConfigurableFilesystemMergeAlgorithm;
import com.griddynamics.maven.plugin.cm.algorithms.AbstractConfigurableMergeAlgorithm;

abstract class AbstractConfigurableTreeMergeAlgorithm
        extends AbstractConfigurableFilesystemMergeAlgorithm<TreeMergeContext>
        implements TreeMergeAlgorithm {

    @Override
    public final boolean canMerge(TreeMergeContext context) {
        return checkPatterns(context.getFileName(), context.getPath()) && canMerge(context.getSourceDir(), context.getTargetDir());
    }

}
