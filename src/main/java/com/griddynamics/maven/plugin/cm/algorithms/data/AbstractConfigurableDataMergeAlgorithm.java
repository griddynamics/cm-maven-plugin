package com.griddynamics.maven.plugin.cm.algorithms.data;

import com.griddynamics.maven.plugin.cm.DataMergeAlgorithm;
import com.griddynamics.maven.plugin.cm.DataMergeContext;
import com.griddynamics.maven.plugin.cm.algorithms.AbstractConfigurableMergeAlgorithm;
import org.apache.maven.plugin.logging.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class AbstractConfigurableDataMergeAlgorithm
        extends AbstractConfigurableMergeAlgorithm<DataMergeContext>
        implements DataMergeAlgorithm {

    private static final XPath X_PATH = XPathFactory.newInstance().newXPath();

    private static final XPathExpression[] EMPTY_STRING_ARRAY = {};

    private static final String INCLUDES = "includes";
    private static final String EXCLUDES = "excludes";

    private XPathExpression[] includes;
    private XPathExpression[] excludes;

    @Override
    public final boolean canMerge(DataMergeContext mergeContext) {
        return checkPatterns(mergeContext) &&
                canMerge(mergeContext.getSourceNode(), mergeContext.getTargetNode(), mergeContext.getParentTargetNode());
    }

    private boolean checkPatterns(DataMergeContext mergeContext) {
        Document document = mergeContext.getSourceDocument();
        Node node = mergeContext.getSourceNode();
        Log log = mergeContext.getLog();
        if (checkPatterns(document, node, includes, log)) {
            return true;
        }
        if (checkPatterns(document, node, excludes, log)) {
            return false;
        }
        return includes.length == 0 || excludes.length != 0;
    }

    private boolean checkPatterns(Document document, Node node, XPathExpression[] expressions, Log log) {
        for (XPathExpression include : includes) {
            try {
                NodeList nodeList = (NodeList) include.evaluate(document, XPathConstants.NODESET);
                for (int i = 0; i < nodeList.getLength(); i ++) {
                    if (nodeList.item(i).isSameNode(node)) {
                        return true;
                    }
                }
            } catch (XPathExpressionException e) {
                if (log.isDebugEnabled()) {
                    log.debug("Unable to evaluate XPath", e);
                }
            }
        }
        return false;
    }

    protected boolean canMerge(Node sourceNode, Node targetNode, Node parentTargetNode) {
        return true;
    }

    @Override
    protected void configure(Properties properties) {
        this.includes = buildXPathArray(properties.getProperty(INCLUDES));
        this.excludes = buildXPathArray(properties.getProperty(EXCLUDES));
    }

    private XPathExpression[] buildXPathArray(String list) {
        if (list == null) {
            return EMPTY_STRING_ARRAY;
        }
        String[] includesArray = list.split(";");
        int size = includesArray.length;
        List<XPathExpression> itemList = new ArrayList<>(size);
        for (String item : includesArray) {
            try {
                itemList.add(X_PATH.compile(item.trim()));
            } catch (XPathExpressionException e) {
                throw new IllegalStateException("Unable to configure XPath", e);
            }
        }
        return itemList.toArray(new XPathExpression[size]);
    }

}
