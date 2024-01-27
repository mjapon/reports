package com.mavil.reports.controller;

import com.mavil.reports.repository.TParamRepository;
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
@RequestMapping("/receta")
public class ReportRecetaController extends ReportBaseController{

    @Autowired
    private TParamRepository paramRepository;

    @GetMapping("get")
    public ResponseEntity<ByteArrayResource> getReport(@RequestParam Integer emp, @RequestParam Integer ccm) throws JRException, IOException, SQLException {

        String esquema = getEmpEsquema(emp);

        String pathReporte = paramRepository.getParamValue(esquema, "rutaReceta");
        String pathFondo = paramRepository.getParamValue(esquema, "pathFondoRec");

        Map parametros = new HashMap();
        parametros.put("pcod_consulta", ccm);
        parametros.put("esquema", esquema);
        parametros.put("pathfondo",  pathFondo);

        byte[] reportContent = jasperReportService.runPdfReport(pathReporte, parametros);
        String reportName = String.format("Receta_%d.pdf", ccm);
        return buildPDFResponse(reportName, reportContent);
    }
}
