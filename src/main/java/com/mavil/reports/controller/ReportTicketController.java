package com.mavil.reports.controller;


import com.mavil.reports.repository.TParamRepository;
import com.mavil.reports.service.JasperReportService;
import com.mavil.reports.util.Constants;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ticket")
public class ReportTicketController extends ReportBaseController {

    @Autowired
    private JasperReportService jasperReportService;

    @Autowired
    private TParamRepository paramRepository;

    @CrossOrigin(origins = Constants.ALLOWED_ORIGINS)
    @GetMapping("{emp}/{code}")
    public ResponseEntity<ByteArrayResource> getTicketByCode(@PathVariable Integer emp, @PathVariable String code) throws JRException, IOException, SQLException {
        String esquema = getEmpEsquema(emp);

        String pathReporte = paramRepository.getParamValue(esquema, "pathReporteTicket");
        Map parametros = new HashMap();
        parametros.put("ticketid", Integer.valueOf(code));

        byte[] reportContent = jasperReportService.runPdfReport(pathReporte, parametros);
        return buildPDFResponse("Ticket.pdf", reportContent);
    }
}