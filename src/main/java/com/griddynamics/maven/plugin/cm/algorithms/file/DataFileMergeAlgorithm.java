package com.griddynamics.maven.plugin.cm.algorithms.file;

import com.griddynamics.maven.plugin.cm.DataMergeContext;
import com.griddynamics.maven.plugin.cm.DataProcessor;
import com.griddynamics.maven.plugin.cm.FileMergeContext;
import com.griddynamics.maven.plugin.cm.MergeException;

import java.io.File;
import java.io.IOException;

public class DataFileMergeAlgorithm extends AbstractConfigurableFileMergeAlgorithm {

    @Override
    public boolean canMerge(FileMergeContext mergeContext) {
        File source = mergeContext.getSourceFile();
        File target = mergeContext.getTargetFile();
        if (!target.isFile()) {
            return false;
        }
        if (!source.getName().equals(target.getName())) {
            return false;
        }
        for (DataProcessor dataProcessor : mergeContext.getDataProcessors()) {
            if (dataProcessor.canProcess(source)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void merge(FileMergeContext mergeContext) throws IOException, MergeException {
        DataMergeContext dataMergeContext = new DataMergeContext(mergeContext);
        dataMergeContext.merge();
    }

}
