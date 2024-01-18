package com.mavil.reports.controller;

import com.mavil.reports.repository.TParamRepository;
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
@RequestMapping("/abono")
public class ReportAbonoController extends ReportBaseController {

    @Autowired
    private TParamRepository paramRepository;

    @GetMapping("get")
    public ResponseEntity<ByteArrayResource> getReport(@RequestParam Integer emp, @RequestParam Integer trn) throws JRException, IOException, SQLException {

        String esquema = getEmpEsquema(emp);

        Map<String, Object> trnDataMap = paramRepository.getTransaccData(esquema, trn);
        String secCodigo = String.valueOf(trnDataMap.get("sec_codigo"));

        String pathReporte = paramRepository.getParamValue(esquema, "pathReporteAbo");
        String pathFondo = paramRepository.getParamValue(esquema, "pathFondoAbo", secCodigo);

        Map parametros = new HashMap();
        parametros.put("ptrncod", trn);
        parametros.put("pesquema", esquema);
        parametros.put("pathfondo", pathFondo);

        byte[] reportContent = jasperReportService.runPdfReport(pathReporte, parametros);

        String reportName = String.format("Abono_%d.pdf", trn);
        return buildResponse(reportName, reportContent);
    }

}
