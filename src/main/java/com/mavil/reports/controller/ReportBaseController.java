package com.mavil.reports.controller;

import com.mavil.reports.service.EmpresaService;
import com.mavil.reports.service.JasperReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ReportBaseController {

    @Autowired
    protected JasperReportService jasperReportService;

    @Autowired
    protected EmpresaService empresaService;

    protected ResponseEntity<ByteArrayResource> buildResponse(String fileName, byte[] reportContent) {

        ByteArrayResource resource = new ByteArrayResource(reportContent);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline()
                                .filename(fileName)
                                .build().toString())
                .body(resource);
    }

    protected String getEmpEsquema(Integer empCodigo) {
        return empresaService.getEsquema(empCodigo);
    }

}
