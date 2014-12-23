package com.griddynamics.maven.plugin.cm;

import org.w3c.dom.*;

import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;

public final class DataMergeContext extends MergeContext {

    private static final String DOCUMENT_X_PATH = "/".intern();

    private static final XPath X_PATH = XPathFactory.newInstance().newXPath();

    private final Document sourceDocument;

    private final Document targetDocument;

    private final DataProcessor dataProcessor;

    private final String xPath;

    private XPathExpression xPathExpression;

    private final DataMergeContext parent;

    private final File targetFile;

    public DataMergeContext(FileMergeContext parent) throws DataMergeException {
        super(parent);
        File sourceFile = parent.getSourceFile();
        File targetFile = parent.getTargetFile();
        DataProcessor dataProcessor = null;
        for (DataProcessor supposedDataProcessor : getDataProcessors()) {
            if (supposedDataProcessor.canProcess(sourceFile)) {
                dataProcessor = supposedDataProcessor;
                break;
            }
        }
        if (dataProcessor == null) {
            throw new IllegalStateException("Unable to find data processor for " + sourceFile);
        }
        this.dataProcessor = dataProcessor;
        try {
            this.sourceDocument = dataProcessor.read(sourceFile);
            this.targetDocument = dataProcessor.read(targetFile);
        } catch (IOException e) {
            throw new DataMergeException("Unable to parse input files for merge", e);
        }
        xPath = DOCUMENT_X_PATH;
        this.targetFile = parent.getTargetFile();
        this.parent = null;
    }

    private DataMergeContext(
            DataMergeContext parent,
            String xPath
        ) {
        super(parent);
        this.sourceDocument = parent.sourceDocument;
        this.targetDocument = parent.targetDocument;
        this.dataProcessor = null;
        if (DOCUMENT_X_PATH.equals(parent.xPath)) {
            this.xPath = xPath;
        } else {
            this.xPath = parent.xPath + xPath;
        }
        this.targetFile = null;
        this.parent = parent;
    }

    public DataMergeContext(DataMergeContext parent, Element element, int number) {
        this(parent, String.format("/%s[%d]", element.getTagName(), number));
    }

    public DataMergeContext(DataMergeContext parent, Attr attr) {
        this(parent, String.format("/@%s", attr.getName()));
    }

    public DataMergeContext(DataMergeContext parent, Text text) {
        this(parent, String.format("='%s'", text.getData().replace("'", "\\'")));
    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public String getFileName() {
        return null;
    }

    @Override
    public void merge() throws IOException, MergeException {
        for (DataMergeAlgorithm dataMergeAlgorithm : getDataMergeAlgorithms()) {
            if (dataMergeAlgorithm.canMerge(this)) {
                if (getLog().isDebugEnabled()) {
                    getLog().debug("Merging " + xPath + " using " + dataMergeAlgorithm.getClass());
                }
                dataMergeAlgorithm.merge(this);
                store();
                return;
            }
        }
    }

    private void store() throws IOException {
        if (dataProcessor != null) {
            dataProcessor.write(targetFile, getTargetDocument());
        }
    }

    public String getXPath() {
        return xPath;
    }

    public Document getSourceDocument() {
        return sourceDocument;
    }

    public Document getTargetDocument() {
        return targetDocument;
    }

    public Node getSourceNode() {
        return getNode(getSourceDocument());
    }

    public Node getTargetNode() {
        return getNode(getTargetDocument());
    }

    public Node getParentTargetNode() {
        if (parent == null) {
            return null;
        }
        return parent.getTargetNode();
    }

    private Node getNode(Document document) {
        try {
            return (Node) getXPathExpression().evaluate(document, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            getLog().error("Unable to get node by XPath", e);
            return null;
        }
    }

    private XPathExpression getXPathExpression() {
        if (xPathExpression == null) {
            try {
                xPathExpression = X_PATH.compile(xPath);
            } catch (XPathExpressionException e) {
                throw new IllegalStateException("Unable to compile XPath " + getXPath(), e);
            }
        }
        return xPathExpression;
    }

}
