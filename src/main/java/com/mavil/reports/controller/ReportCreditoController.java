package com.mavil.reports.controller;

import com.mavil.reports.repository.TParamRepository;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/credito")
public class ReportCreditoController extends ReportBaseController{

    @Autowired
    private TParamRepository paramRepository;

    @GetMapping("{emp}/{codcred}")
    public ResponseEntity<ByteArrayResource> getReport(@PathVariable Integer emp, @PathVariable Integer codcred) throws JRException, IOException, SQLException {

        String esquema = getEmpEsquema(emp);
        String pathReporte = paramRepository.getParamValue(esquema, "pathReportCred");

        Map parametros = new HashMap();
        parametros.put("pesquema", esquema);
        parametros.put("pcredid", codcred);

        byte[] reportContent = jasperReportService.runPdfReport(pathReporte, parametros);
        String reportName = String.format("Credito_%d.pdf", codcred);
        return buildPDFResponse(reportName, reportContent);
    }
}
