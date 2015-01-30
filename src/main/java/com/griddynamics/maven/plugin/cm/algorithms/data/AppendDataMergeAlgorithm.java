package com.griddynamics.maven.plugin.cm.algorithms.data;

import com.griddynamics.maven.plugin.cm.DataMergeContext;
import com.griddynamics.maven.plugin.cm.MergeException;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.IOException;

public class AppendDataMergeAlgorithm extends AbstractConfigurableDataMergeAlgorithm {

    @Override
    protected boolean canMerge(Node sourceNode, Node targetNode, Node parentTargetNode) {
        return sourceNode != null && parentTargetNode != null &&
                (sourceNode instanceof Comment || parentTargetNode instanceof Element);
    }

    @Override
    public void merge(DataMergeContext mergeContext) throws IOException, MergeException {
        Node node = mergeContext.getSourceNode().cloneNode(true);
        mergeContext.getTargetDocument().adoptNode(node);
        mergeContext.getParentTargetNode().appendChild(node);
    }

}
