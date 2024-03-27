package com.mavil.reports.controller;

import com.mavil.reports.repository.TParamRepository;
import com.mavil.reports.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/historia-clinica")
@Slf4j
public class HistoriaClinicaController extends ReportBaseController {

    @Autowired
    private TParamRepository paramRepository;

    @CrossOrigin(origins = Constants.ALLOWED_ORIGINS)
    @GetMapping("{emp}/{ch}")
    public ResponseEntity<ByteArrayResource> getReporte(@PathVariable Integer emp, @PathVariable Integer ch) {

        String esquema = getEmpEsquema(emp);
        String rutaReporte = paramRepository.getParamValue(esquema, "pathReporteHC");

        Map parametros = new HashMap();
        parametros.put("codhist", ch);
        parametros.put("esquema", esquema);
        try {
            byte[] reportContent = jasperReportService.runPdfReport(rutaReporte, parametros);
            String reportName = String.format("Historia_Clinica_%s.pdf", ch);
            return buildPDFResponse(reportName, reportContent);

        } catch (SQLException e) {
            log.error("Error al tratar de ejecutar reporte Historia Clinica", e);
            return buildHtmlMessage("Se produjo un error al tratar de generar reporte Historia Clinica");
        }

    }
}
