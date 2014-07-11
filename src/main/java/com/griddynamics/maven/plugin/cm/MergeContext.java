package com.griddynamics.maven.plugin.cm;

import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class MergeContext {

    private final File sourceBaseDir;

    private final File targetBaseDir;

    private final List<FileMergeAlgorithm> fileMergeAlgorithms;

    private final List<TreeMergeAlgorithm> treeMergeAlgorithms;

    private final Log log;

    protected MergeContext(File sourceBaseDir, File targetBaseDir, List<FileMergeAlgorithm> fileMergeAlgorithms, List<TreeMergeAlgorithm> treeMergeAlgorithms, Log log) {
        if (!sourceBaseDir.isDirectory()) {
            throw new IllegalArgumentException("Provided source base directory '" + sourceBaseDir + "' is not actual directory");
        }
        if (targetBaseDir.exists() && !targetBaseDir.isDirectory()) {
            throw new IllegalArgumentException("Provided target base directory '" + targetBaseDir + "' is not actual directory");
        }
        this.sourceBaseDir = sourceBaseDir;
        this.targetBaseDir = targetBaseDir;
        this.fileMergeAlgorithms = fileMergeAlgorithms;
        this.treeMergeAlgorithms = treeMergeAlgorithms;
        this.log = log;
    }

    public MergeContext(MergeContext parent) {
        this(parent.sourceBaseDir, parent.targetBaseDir, parent.fileMergeAlgorithms, parent.treeMergeAlgorithms, parent.log);
    }

    public final File getSourceBaseDir() {
        return sourceBaseDir;
    }

    public final File getTargetBaseDir() {
        return targetBaseDir;
    }

    public List<FileMergeAlgorithm> getFileMergeAlgorithms() {
        return fileMergeAlgorithms;
    }

    public List<TreeMergeAlgorithm> getTreeMergeAlgorithms() {
        return treeMergeAlgorithms;
    }

    public Log getLog() {
        return log;
    }

    public abstract String getPath();

    public abstract String getFileName();

    public abstract void merge() throws IOException, MergeException;

}
