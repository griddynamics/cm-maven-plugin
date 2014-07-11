package com.griddynamics.maven.plugin.cm.algorithms.tree;

import com.griddynamics.maven.plugin.cm.TreeMergeAlgorithm;
import com.griddynamics.maven.plugin.cm.TreeMergeContext;
import com.griddynamics.maven.plugin.cm.algorithms.AbstractConfigurableMergeAlgorithm;

abstract class AbstractConfigurableTreeMergeAlgorithm
        extends AbstractConfigurableMergeAlgorithm<TreeMergeContext>
        implements TreeMergeAlgorithm {

    @Override
    public final boolean canMerge(TreeMergeContext context) {
        return checkPatterns(context.getFileName(), context.getPath()) && canMerge(context.getSourceDir(), context.getTargetDir());
    }

}
