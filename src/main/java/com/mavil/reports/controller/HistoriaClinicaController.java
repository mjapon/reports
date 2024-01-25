package com.mavil.reports.controller;

import com.mavil.reports.repository.TParamRepository;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/historiaClinica")
@Slf4j
public class HistoriaClinicaController extends ReportBaseController {

    @Autowired
    private TParamRepository paramRepository;

    @GetMapping("get")
    public ResponseEntity<ByteArrayResource> getReporte(@RequestParam Integer emp, @RequestParam String ch) {

        String esquema = getEmpEsquema(emp);
        String rutaReporte = paramRepository.getParamValue(esquema, "pathReporteHC");

        Map parametros = new HashMap();
        parametros.put("codhist", ch);
        parametros.put("esquema", esquema);
        try {
            byte[] reportContent = jasperReportService.runPdfReport(rutaReporte, parametros);
            String reportName = String.format("Historia_Clinica_%d.pdf", ch);
            return buildPDFResponse(reportName, reportContent);

        } catch (SQLException e) {
            log.error("Error al tratar de ejecutar reporte Historia Clinica", e);
            return buildHtmlMessage("Se produjo un error al tratar de generar reporte Historia Clinica");
        }

    }
}
