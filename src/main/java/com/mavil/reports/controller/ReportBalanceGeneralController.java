package com.mavil.reports.controller;

import com.mavil.reports.service.DocumentGenerator;
import com.mavil.reports.service.ExcelReporteGenService;
import com.mavil.reports.util.Constants;
import com.mavil.reports.util.DataMapper;
import com.mavil.reports.vo.GenBalanceRequestVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@RestController
@RequestMapping("/contable")
@Slf4j
public class ReportBalanceGeneralController extends ReportBaseController {

    @Autowired
    ExcelReporteGenService service;


    @Autowired
    private DocumentGenerator documentGenerator;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Autowired
    private DataMapper dataMapper;


    @CrossOrigin(origins = Constants.ALLOWED_ORIGINS)
    @PostMapping("/genBalanceGeneral")
    public ResponseEntity<byte[]> downloadExcelFeaturesItemsByBarCodes(@RequestBody GenBalanceRequestVo requestVo) {
        try {
            byte[] excelBytes = service.generateExcel(requestVo);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
            headers.setContentDispositionFormData("attachment", "ART_FEAT.xlsx");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            if (excelBytes.length == 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(excelBytes);
            }
        } catch (Exception ex) {
            log.error("Error downloadExcelFeaturesItemsByBarCodes: ", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = Constants.ALLOWED_ORIGINS)
    @PostMapping(value = "/genPdf")
    public ResponseEntity<ByteArrayResource> generateDocument(@RequestBody GenBalanceRequestVo requestVo) {

        Context dataContext = dataMapper.setData(requestVo);

        String finalHtml = springTemplateEngine.process("template", dataContext);

        byte[] reportContent = documentGenerator.htmlToPdf(finalHtml);

        String reportName = String.format("Reporte.pdf");
        return buildPDFResponse(reportName, reportContent);

    }

}
