package com.griddynamics.maven.plugin.cm.algorithms.file;

import com.griddynamics.maven.plugin.cm.FileMergeContext;
import com.griddynamics.maven.plugin.cm.MergeException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class CnfFileMergeAlgorithm extends AbstractConfigurableFileMergeAlgorithm {

    @Override
    protected boolean canMerge(File source, File target) {
        String sourceName = source.getName();
        return (sourceName.endsWith(".ini") || sourceName.endsWith(".cnf")) && sourceName.equals(target.getName());
    }

    @Override
    public void merge(FileMergeContext mergeContext) throws IOException, MergeException {
        try {
            HierarchicalINIConfiguration targetConfiguration = new HierarchicalINIConfiguration();
            HierarchicalINIConfiguration sourceConfiguration = new HierarchicalINIConfiguration();
            targetConfiguration.setDelimiterParsingDisabled(true);
            sourceConfiguration.setDelimiterParsingDisabled(true);
            targetConfiguration.load(mergeContext.getTargetFile());
            sourceConfiguration.load(mergeContext.getSourceFile());
            for (String sectionName : sourceConfiguration.getSections()) {
                SubnodeConfiguration targetSection = targetConfiguration.getSection(sectionName);
                SubnodeConfiguration sourceSection = sourceConfiguration.getSection(sectionName);
                SortedProperties sortedProperties = new SortedProperties();
                Iterator<String> targetPropertyNames = targetSection.getKeys();
                while (targetPropertyNames.hasNext()) {
                    String targetPropertyName = targetPropertyNames.next();
                    sortedProperties.put(targetPropertyName, targetSection.getString(targetPropertyName));
                }
                Iterator<String> sourcePropertyNames = sourceSection.getKeys();
                while (sourcePropertyNames.hasNext()) {
                    String sourcePropertyName = sourcePropertyNames.next();
                    sortedProperties.put(sourcePropertyName, sourceSection.getString(sourcePropertyName));
                }
                targetSection.clear();
                for (Map.Entry<Object, Object> entry : sortedProperties.entrySet()) {
                    targetSection.addProperty(entry.getKey().toString(), entry.getValue());
                }
            }
            targetConfiguration.save(mergeContext.getTargetFile());
        } catch (ConfigurationException e) {
            throw new MergeException("Unable to read file to merge", e);
        }
    }

}
