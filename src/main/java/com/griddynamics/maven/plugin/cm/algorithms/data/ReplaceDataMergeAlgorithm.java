package com.griddynamics.maven.plugin.cm.algorithms.data;

import com.griddynamics.maven.plugin.cm.DataMergeContext;
import com.griddynamics.maven.plugin.cm.MergeException;
import org.w3c.dom.Node;

import java.io.IOException;

public class ReplaceDataMergeAlgorithm extends AbstractConfigurableDataMergeAlgorithm {

    @Override
    public boolean canMerge(Node sourceNode, Node targetNode) {
        return sourceNode != null && targetNode != null;
    }

    @Override
    public void merge(DataMergeContext mergeContext) throws IOException, MergeException {
        Node node = mergeContext.getSourceNode().cloneNode(true);
        mergeContext.getTargetDocument().adoptNode(node);
        mergeContext.getParentTargetNode().removeChild(mergeContext.getTargetNode());
        mergeContext.getParentTargetNode().appendChild(node);
    }

}
