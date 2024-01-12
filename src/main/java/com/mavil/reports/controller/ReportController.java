package com.mavil.reports.controller;


import com.mavil.reports.service.JasperReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/reportes")
public class ReportController {

    @Autowired
    JasperReportService jasperReportService;

    @GetMapping("ticket")
    public ResponseEntity<ByteArrayResource> getItemReport() throws JRException, IOException, SQLException {

        byte[] reportContent = jasperReportService.getItemReport();

        ByteArrayResource resource = new ByteArrayResource(reportContent);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("item-report.pdf")
                                .build().toString())
                .body(resource);
    }
}