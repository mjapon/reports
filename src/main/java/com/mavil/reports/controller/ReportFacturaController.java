package com.mavil.reports.controller;

import com.mavil.reports.repository.TParamRepository;
import com.mavil.reports.util.Constants;
import com.mavil.reports.vo.TransaccDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/factura")
public class ReportFacturaController extends ReportBaseController {

    @Autowired
    private TParamRepository paramRepository;

    @CrossOrigin(origins = Constants.ALLOWED_ORIGINS)
    @GetMapping("{emp}/{trn}")
    public ResponseEntity<ByteArrayResource> getReport(@PathVariable Integer emp, @PathVariable Integer trn) throws SQLException {

        String esquema = getEmpEsquema(emp);

        TransaccDataVo transaccDataVo = paramRepository.getTransaccData(esquema, trn);

        Boolean isNotaVenta = paramRepository.isNotaVenta(Integer.valueOf(transaccDataVo.getTraCodigo()));
        String secCodigo = transaccDataVo.getSecCodigo();

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
