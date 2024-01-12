package com.mavil.reports.controller;


import com.mavil.reports.service.JasperReportService;
import com.mavil.reports.util.Constants;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/tickets")
public class ReportTicketController extends ReportBaseController {

    @Autowired
    JasperReportService jasperReportService;

    @GetMapping("get")
    public ResponseEntity<ByteArrayResource> getTicketByCode(@RequestParam String code) throws JRException, IOException, SQLException {

        Map parametros = new HashMap();
        parametros.put("ticketid", Integer.valueOf(code));

        byte[] reportContent = jasperReportService.runPdfReport(Constants.PATH_REPORT_TICKETS, parametros);

        return buildResponse("ticket.pdf", reportContent);

    }
}