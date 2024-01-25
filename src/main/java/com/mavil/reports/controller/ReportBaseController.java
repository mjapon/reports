package com.mavil.reports.controller;

import com.mavil.reports.service.EmpresaService;
import com.mavil.reports.service.JasperReportService;
import com.mavil.reports.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ReportBaseController {

    @Autowired
    protected JasperReportService jasperReportService;

    @Autowired
    protected EmpresaService empresaService;

    protected ResponseEntity<ByteArrayResource> buildPDFResponse(String fileName, byte[] reportContent) {

        Map<String, String> headers = Map.of(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.inline()
                .filename(fileName)
                .build().toString());

        return auxBuildResponse(reportContent, MediaType.APPLICATION_PDF, headers);

        /*ByteArrayResource resource = new ByteArrayResource(reportContent);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline()
                                .filename(fileName)
                                .build().toString())
                .body(resource);*/
    }

    protected ResponseEntity<ByteArrayResource> buildHtmlMessage(String message) {
        Map<String, String> headers = Map.of(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.inline()
                .build().toString());
        return auxBuildResponse(message.getBytes(StandardCharsets.UTF_8), MediaType.TEXT_HTML, headers);
    }

    protected ResponseEntity<ByteArrayResource> buildCustomTypeResponse(String fileName, String fileExt, byte[] content) {

        return auxBuildResponse(content,
                FileUtils.getMediaType(fileExt),
                FileUtils.getHeaders(fileName, fileExt));

    }


    protected String getEmpEsquema(Integer empCodigo) {
        return empresaService.getEsquema(empCodigo);
    }

    private ResponseEntity<ByteArrayResource> auxBuildResponse(
            byte[] reportContent,
            MediaType contentType,
            Map<String, String> keyValueHeaders) {
        ByteArrayResource resource = new ByteArrayResource(reportContent);
        ResponseEntity.BodyBuilder bodyBuilder =
                ResponseEntity.ok()
                        .contentType(contentType)
                        .contentLength(resource.contentLength());
        if (keyValueHeaders != null && !keyValueHeaders.isEmpty()) {
            for (String header : keyValueHeaders.keySet()) {
                bodyBuilder = bodyBuilder.header(header, keyValueHeaders.get(header));
            }
        }

        return bodyBuilder.body(resource);
    }
}
