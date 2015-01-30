package com.griddynamics.maven.plugin.cm;

import java.util.Properties;

public class DataProcessorModel {

    private String implementation;

    private Properties configuration;

    public DataProcessorModel() {
        this(null, null);
    }

    public DataProcessorModel(String implementation, Properties configuration) {
        this.implementation = implementation;
        this.configuration = configuration;
    }

    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }

    public String getImplementation() {
        return implementation;
    }

    public void setConfiguration(Properties configuration) {
        this.configuration = configuration;
    }

    public Properties getConfiguration() {
        return configuration;
    }

}
