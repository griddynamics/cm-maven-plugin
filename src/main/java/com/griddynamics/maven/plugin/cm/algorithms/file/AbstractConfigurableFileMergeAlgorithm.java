package com.griddynamics.maven.plugin.cm.algorithms.file;

import com.griddynamics.maven.plugin.cm.FileMergeAlgorithm;
import com.griddynamics.maven.plugin.cm.FileMergeContext;
import com.griddynamics.maven.plugin.cm.algorithms.AbstractConfigurableMergeAlgorithm;

public abstract class AbstractConfigurableFileMergeAlgorithm
    extends AbstractConfigurableMergeAlgorithm<FileMergeContext>
    implements FileMergeAlgorithm {

    @Override
    public boolean canMerge(FileMergeContext mergeContext) {
        return checkPatterns(mergeContext.getFileName(), mergeContext.getPath()) && canMerge(mergeContext.getSourceFile(), mergeContext.getTargetFile());
    }

}
