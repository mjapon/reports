package com.mavil.reports.controller;

import com.mavil.reports.repository.TParamRepository;
import com.mavil.reports.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/plan-tratamiento")
public class PlanTratamientoController extends ReportBaseController {


    @Autowired
    private TParamRepository paramRepository;

    @CrossOrigin(origins = Constants.ALLOWED_ORIGINS)
    @GetMapping("{emp}/{cod}")
    public ResponseEntity<ByteArrayResource> getReport(@PathVariable Integer emp, @PathVariable Integer cod) throws SQLException {

        String empEsquema = getEmpEsquema(emp);

        String pathLogo = paramRepository.getParamValue(empEsquema, "pathlogoplnod");
        String pathReporte = paramRepository.getParamValue(empEsquema, "pathPlanTrata");

        Map parametros = new HashMap();
        parametros.put("codpt", cod);
        parametros.put("esquema", empEsquema);
        parametros.put("pathlogo", pathLogo);

        byte[] reportContent = jasperReportService.runPdfReport(pathReporte, parametros);

        String reportName = String.format("PlanTratamiento_%d.pdf", cod);
        return buildPDFResponse(reportName, reportContent);

    }
}
