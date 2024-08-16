package com.accounting_manager.AccountingCore;

import lombok.extern.log4j.Log4j2;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class to perform XSLT transformation on XML files.
 */
@Log4j2
public class XsltTransformer {

    // Path where XSLT templates are located
    public static String xsltPath;

    // XSLT template filename
    private String xsltTemplate;

    /**
     * Constructor to initialize XSLT template.
     *
     * @param xsltTemplate Name of the XSLT template file (without .xslt extension)
     */
    public XsltTransformer(String xsltTemplate) {
        this.xsltTemplate = xsltTemplate;

        // Determine the correct path for XSLT templates based on environment
        if (Files.exists(Path.of("AccountingEngine/src/main/resources/"))) {
            xsltPath = "AccountingCore/src/main/resources/xslt/";
        } else {
            xsltPath = "/app/src/main/resources/xslt/";
        }
    }

    /**
     * Transforms XML file using the specified XSLT template.
     *
     * @param xmlFilePath Path to the XML file to transform
     * @return Transformed XML content as a string
     * @throws Exception If there is an error during transformation
     */
    public String transform(String xmlFilePath) throws Exception {
        StreamSource xsltStreamSource = new StreamSource(new File(xsltPath + xsltTemplate + ".xslt"));
        StreamSource xmlStreamSource = new StreamSource(new File(xmlFilePath));

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer(xsltStreamSource);

        // Create a StringWriter to capture the transformed XML
        Writer resultWriter = new StringWriter();
        transformer.transform(xmlStreamSource, new StreamResult(resultWriter));

        return resultWriter.toString();
    }

    /**
     * Saves transformed XML content to a specified output path.
     *
     * @param transformedXml Transformed XML content to save
     * @param outputPath     Path where transformed XML should be saved
     * @throws Exception If there is an error while saving the XML
     */
    public void saveTransformedXml(String transformedXml, String outputPath) throws Exception {
        try (Writer writer = new FileWriter(outputPath)) {
            writer.write(transformedXml);
        }
    }
}
