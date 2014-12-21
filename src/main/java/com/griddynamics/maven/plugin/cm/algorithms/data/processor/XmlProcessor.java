package com.griddynamics.maven.plugin.cm.algorithms.data.processor;

import com.griddynamics.maven.plugin.cm.DataProcessor;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class XmlProcessor implements DataProcessor {

    private static final DocumentBuilder DOCUMENT_BUILDER;

    private static final Transformer TRANSFORMER;

    static {
        DocumentBuilder documentBuilder;
        Transformer transformer;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException | ParserConfigurationException e) {
            throw new ExceptionInInitializerError(e);
        }
        DOCUMENT_BUILDER = documentBuilder;
        TRANSFORMER = transformer;
    }

    @Override
    public boolean canProcess(File file) {
        return file.getName().toLowerCase().endsWith(".xml");
    }

    @Override
    public Document read(File file) throws IOException {
        try {
            return DOCUMENT_BUILDER.parse(file);
        } catch (SAXException e) {
            throw new IOException("Unable to parse document", e);
        }
    }

    @Override
    public void write(File file, Document document) throws IOException {
        try {
            TRANSFORMER.transform(new DOMSource(document), new StreamResult(file));
        } catch (TransformerException e) {
            throw new IOException("Unable to transform document into file", e);
        }
    }

}
