package com.mavil.reports.controller.lists;

import com.mavil.reports.controller.ReportBaseController;
import com.mavil.reports.service.DocumentGenerator;
import com.mavil.reports.service.ExcelReporteGenService;
import com.mavil.reports.service.TGridService;
import com.mavil.reports.util.Constants;
import com.mavil.reports.util.GridDataMapper;
import com.mavil.reports.vo.GenerateReportRequestVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@RestController
@RequestMapping("/grid")
@Slf4j
public class GridReportController extends ReportBaseController {

    @Autowired
    ExcelReporteGenService service;

    @Autowired
    private TGridService tGridService;

    @Autowired
    private DocumentGenerator documentGenerator;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Autowired
    private GridDataMapper dataMapper;


    @CrossOrigin(origins = Constants.ALLOWED_ORIGINS)
    @PostMapping("/excel")
    public ResponseEntity<byte[]> downloadExcelBalanceGeneral(@RequestBody GenerateReportRequestVo requestVo) {
        try {
            byte[] excelBytes = tGridService.generateExcel(requestVo);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
            headers.setContentDispositionFormData("attachment", "REPORT.xlsx");
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
    @PostMapping(value = "/pdf")
    public ResponseEntity<ByteArrayResource> generateDocument(@RequestBody GenerateReportRequestVo requestVo) {

        Context dataContext = dataMapper.setData(requestVo);

        String finalHtml = springTemplateEngine.process("template-grid", dataContext);

        byte[] reportContent = documentGenerator.htmlToPdf(finalHtml, true);

        String reportName = String.format("REPORT.pdf");
        return buildPDFResponse(reportName, reportContent);

    }


}
