package com.griddynamics.maven.plugin.cm.algorithms.data;

import com.griddynamics.maven.plugin.cm.DataMergeContext;
import com.griddynamics.maven.plugin.cm.MergeException;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RecursiveDataMergeAlgorithm extends AbstractConfigurableDataMergeAlgorithm {

    @Override
    public boolean canMerge(Node sourceNode, Node targetNode) {
        return sourceNode != null && targetNode != null;
    }

    @Override
    public void merge(DataMergeContext mergeContext) throws IOException, MergeException {
        Node sourceNode = mergeContext.getSourceNode();
        Node sourceChild = sourceNode.getFirstChild();

        Map<String, Integer> elementCounters = new HashMap<>();
        do {
            if (sourceChild instanceof Element) {
                Element sourceChildElement = (Element) sourceChild;
                String name = sourceChildElement.getTagName();
                Integer number = elementCounters.get(name);
                if (number == null) {
                    number = 1;
                } else {
                    number ++;
                }
                elementCounters.put(name, number);
                new DataMergeContext(mergeContext, sourceChildElement, number).merge();
            } else if (sourceChild instanceof Attr) {
                new DataMergeContext(mergeContext, (Attr) sourceChild);
            } else if (sourceChild instanceof Text) {
                new DataMergeContext(mergeContext, (Text) sourceChild);
            }
        } while ((sourceChild = sourceChild.getNextSibling()) != null);
    }

}
