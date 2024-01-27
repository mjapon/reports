package com.mavil.reports.controller;

import com.mavil.reports.repository.TParamRepository;
import com.mavil.reports.service.JasperReportService;
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
@RequestMapping("/recetaEmpty")
public class ReportRecetaEmptyController extends ReportBaseController {
    @Autowired
    private JasperReportService jasperReportService;

    @Autowired
    private TParamRepository paramRepository;

    @GetMapping("get")
    public ResponseEntity<ByteArrayResource> getRecetaByCode(@RequestParam Integer emp) throws JRException, IOException, SQLException {
        String esquema = getEmpEsquema(emp);
        String pathReporte = paramRepository.getParamValue(esquema, "rutaRecetaEmpty");
        String pathFondo = paramRepository.getParamValue(esquema, "pathFondoRec");
        Map parametros = new HashMap();
        parametros.put("pathfondo", pathFondo);

        byte[] reportContent = jasperReportService.runPdfReport(pathReporte, parametros);
        return buildPDFResponse("Receta.pdf", reportContent);
    }
}
