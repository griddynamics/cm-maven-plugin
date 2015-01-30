package com.griddynamics.maven.plugin.cm;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface DataProcessor {

    boolean canProcess(File file);

    Document read(File file) throws IOException;

    void write(File file, Document document) throws IOException;

}
