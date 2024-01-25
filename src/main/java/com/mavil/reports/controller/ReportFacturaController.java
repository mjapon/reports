package com.mavil.reports.controller;

import com.mavil.reports.repository.TParamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/factura")
public class ReportFacturaController extends ReportBaseController {

    @Autowired
    private TParamRepository paramRepository;

    @GetMapping("get")
    public ResponseEntity<ByteArrayResource> getReport(@RequestParam Integer emp, @RequestParam Integer trn) throws SQLException {

        String esquema = getEmpEsquema(emp);

        Map<String, Object> trnDataMap = paramRepository.getTransaccData(esquema, trn);
        String secCodigo = String.valueOf(trnDataMap.get("sec_codigo"));

        Boolean isNotaVenta = paramRepository.isNotaVenta(trnDataMap);

        String pathFondo = paramRepository.getParamValue(esquema, "pathFondoFact", secCodigo);

        String paramTemplate = "pathReporteFact";
        if (isNotaVenta) {
            pathFondo = paramRepository.getParamValue(esquema, "pathFondoNotaV", secCodigo);
            paramTemplate = "pathReporteNotaV";
        }

        String pathReporte = paramRepository.getParamValue(esquema, paramTemplate, secCodigo);

        Map parametros = new HashMap();
        parametros.put("ptrncod", trn);
        parametros.put("pesquema", esquema);
        parametros.put("pathfondo", pathFondo);

        byte[] reportContent = jasperReportService.runPdfReport(pathReporte, parametros);

        String reportName = String.format("Comprobante_%d.pdf", trn);
        return buildPDFResponse(reportName, reportContent);

    }


}
