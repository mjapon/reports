package com.mavil.reports.service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
public class DocumentGenerator {

    private static final Logger log = LoggerFactory.getLogger(DocumentGenerator.class);

    public byte[] htmlToPdf(String processedHtml, Boolean horizontal) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {

            PdfWriter pdfwriter = new PdfWriter(byteArrayOutputStream);

            PdfDocument pdfDoc = new PdfDocument(pdfwriter);
            if (horizontal) {
                pdfDoc.setDefaultPageSize(PageSize.A4.rotate());
            } else {
                pdfDoc.setDefaultPageSize(PageSize.A4);
            }

            DefaultFontProvider defaultFont = new DefaultFontProvider(false, true, false);

            ConverterProperties converterProperties = new ConverterProperties();

            converterProperties.setFontProvider(defaultFont);

            //HtmlConverter.convertToPdf(processedHtml, pdfwriter, converterProperties);

            HtmlConverter.convertToPdf(new ByteArrayInputStream(processedHtml.getBytes()), pdfDoc);


            byteArrayOutputStream.close();

            byteArrayOutputStream.flush();


            return byteArrayOutputStream.toByteArray();

        } catch (Exception ex) {
            log.error("Error al tratar de convertir html to pdf", ex);
        }

        return null;
    }

    public byte[] htmlToPdf(String processedHtml) {
        return this.htmlToPdf(processedHtml, false);
    }


}