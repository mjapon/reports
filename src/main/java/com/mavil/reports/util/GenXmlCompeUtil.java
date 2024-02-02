package com.mavil.reports.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GenXmlCompeUtil {

    public String genXml(String estadoValue,
                         String numeroAutorizacionValue,
                         String fechaAutorizacionValue,
                         String ambienteValue,
                         String pathDocSigned
    ) throws ParserConfigurationException {

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("autorizacion");
        doc.appendChild(rootElement);

        Element estado = doc.createElement("estado");
        estado.setTextContent(estadoValue);
        rootElement.appendChild(estado);

        Element numeroAutorizacion = doc.createElement("numeroAutorizacion");
        numeroAutorizacion.setTextContent(numeroAutorizacionValue);

        rootElement.appendChild(numeroAutorizacion);

        Element fechaAutorizacion = doc.createElement("fechaAutorizacion");
        fechaAutorizacion.setTextContent(fechaAutorizacionValue);

        rootElement.appendChild(fechaAutorizacion);

        Element ambiente = doc.createElement("ambiente");
        ambiente.setTextContent(ambienteValue);
        rootElement.appendChild(ambiente);

        //String rutaXml = "/Users/manueljapon/Documents/dev/mavil/rutapruebas/docsfirmados/1410202201110516177000110010010000002520000000015.xml";

        try {
            Path path = Paths.get(pathDocSigned);
            String docsigend = Files.readAllLines(path).toString();
            Element comprobante = doc.createElement("comprobante");
            comprobante.setTextContent(String.format("<![CDATA%s]>", doc.createCDATASection(docsigend).getData()));
            rootElement.appendChild(comprobante);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element mensajes = doc.createElement("mensajes");
        rootElement.appendChild(mensajes);

        try {
            return writeXml(doc);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    private String writeXml(Document doc) throws TransformerException {

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);

        return writer.toString();
    }

}
