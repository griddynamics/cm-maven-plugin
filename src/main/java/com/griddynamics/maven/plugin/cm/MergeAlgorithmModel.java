package com.griddynamics.maven.plugin.cm;

import java.util.Properties;

public class MergeAlgorithmModel {

    private String implementation;

    private Properties configuration;

    public MergeAlgorithmModel(String implementation, Properties configuration) {
        this.implementation = implementation;
        this.configuration = configuration;
    }

    public MergeAlgorithmModel() {
        this(null, null);
    }

    public String getImplementation() {
        return implementation;
    }

    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }

    public Properties getProperties() {
        return configuration;
    }

    public void setConfiguration(Properties configuration) {
        this.configuration = configuration;
    }

}
