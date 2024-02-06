package com.mavil.reports.controller;

import com.mavil.reports.repository.TParamRepository;
import com.mavil.reports.vo.TransaccDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/abono")
public class ReportAbonoController extends ReportBaseController {

    @Autowired
    private TParamRepository paramRepository;

    @GetMapping("{emp}/{trn}")
    public ResponseEntity<ByteArrayResource> getReport(@PathVariable Integer emp, @PathVariable Integer trn) throws SQLException {

        String esquema = getEmpEsquema(emp);

        TransaccDataVo transaccData = paramRepository.getTransaccData(esquema, trn);

        String pathReporte = paramRepository.getParamValue(esquema, "pathReporteAbo");
        String pathFondo = paramRepository.getParamValue(esquema, "pathFondoAbo", transaccData.getSecCodigo());

        Map parametros = new HashMap();
        parametros.put("ptrncod", trn);
        parametros.put("pesquema", esquema);
        parametros.put("pathfondo", pathFondo);

        byte[] reportContent = jasperReportService.runPdfReport(pathReporte, parametros);

        String reportName = String.format("Abono_%d.pdf", trn);
        return buildPDFResponse(reportName, reportContent);
    }

}
