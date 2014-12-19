package com.griddynamics.maven.plugin.cm.algorithms.data.processor;

import com.griddynamics.maven.plugin.cm.DataProcessor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

public class CnfProcessor implements DataProcessor {

    private static final DocumentBuilder DOCUMENT_BUILDER;

    static {
        try {
            DOCUMENT_BUILDER = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Override
    public boolean canProcess(File file) {
        return file.getName().endsWith(".cnf");
    }

    @Override
    public Document read(File file) throws IOException {
        Document document = DOCUMENT_BUILDER.newDocument();
        document.appendChild(document.createElement(file.getName()));
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Element root = document.getDocumentElement();
            Element section = null;
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#")) {
                    // ignored
                } else if (line.startsWith("[") && line.endsWith("]")) {
                    section = document.createElement(line.substring(1, line.length() - 1));
                    root.appendChild(section);
                } else if (line.contains("=")) {
                    if (section == null) {
                        throw new IllegalStateException("No section for " + line + " property");
                    }
                    String[] property = line.split("=", 2);
                    Element element = document.createElement(property[0].trim());
                    Text text = document.createTextNode(property[1].trim());
                    element.appendChild(text);
                    section.appendChild(element);
                } else if (!line.isEmpty()) {
                    if (section == null) {
                        throw new IllegalStateException("No section for " + line + " entry");
                    }
                    section.appendChild(document.createElement(line));
                }
            }
        }
        return document;
    }

    @Override
    public void write(File file, Document document) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            Element root = document.getDocumentElement();
            for (Node section = root.getFirstChild(); section != null; section = section.getNextSibling()) {
                if (!(section instanceof Element)) {
                    throwWriteException();
                }
                Element sectionElement = (Element) section;
                writer.write("[" + sectionElement.getTagName() + "]");
                writer.newLine();
                for (Node entry = sectionElement.getFirstChild(); entry != null; entry = entry.getNextSibling()) {
                    if (!(entry instanceof Element)) {
                        throwWriteException();
                    }
                    Element entryElement = (Element) entry;
                    if (entryElement.hasChildNodes()) {
                        if (entryElement.getChildNodes().getLength() != 1) {
                            throwWriteException();
                        }
                        Node node = entryElement.getChildNodes().item(0);
                        if (!(node instanceof Text)) {
                            throwWriteException();
                        }
                        writer.write(entryElement.getTagName() + "=" + ((Text) node).getData());
                    } else {
                        writer.write(entryElement.getTagName());
                    }
                    writer.newLine();
                }
            }
        }
    }

    private void throwWriteException() {
        throw new IllegalArgumentException("This document does not conforms CNF structure");
    }

}
