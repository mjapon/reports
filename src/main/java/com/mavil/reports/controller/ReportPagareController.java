package com.mavil.reports.controller;

import com.mavil.reports.repository.TParamRepository;
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
@RequestMapping("/pagare")
public class ReportPagareController extends ReportBaseController{

    @Autowired
    private TParamRepository paramRepository;

    @GetMapping("{emp}/{codcred}")
    public ResponseEntity<ByteArrayResource> getReport(@PathVariable Integer emp, @PathVariable Integer codcred) throws JRException, IOException, SQLException {

        String esquema = getEmpEsquema(emp);

        String pathReporte = paramRepository.getParamValue(esquema, "pathReportPagare");

        Map parametros = new HashMap();
        parametros.put("pesquema", esquema);
        parametros.put("pcredid", codcred);

        byte[] reportContent = jasperReportService.runPdfReport(pathReporte, parametros);
        String reportName = String.format("ReportePagare_%d.pdf", codcred);
        return buildPDFResponse(reportName, reportContent);
    }

}
