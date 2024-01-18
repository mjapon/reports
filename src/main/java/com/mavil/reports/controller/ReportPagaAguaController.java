package com.mavil.reports.controller;

import com.mavil.reports.repository.TParamRepository;
import com.mavil.reports.vo.ComproAguaRequestVo;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/comproAgua")
public class ReportPagaAguaController extends ReportBaseController {

    @Autowired
    private TParamRepository paramRepository;

    @PostMapping("generate")
    public ResponseEntity<ByteArrayResource> getReport(@RequestBody ComproAguaRequestVo request) throws JRException, IOException, SQLException {

        String esquema = getEmpEsquema(request.getEmpCodigo());

        String pathFondo = paramRepository.getParamValue(esquema, "pathFondoAgua");
        String pathReporte = paramRepository.getParamValue(esquema, "pathReporteAgua");

        Map parametros = new HashMap();
        parametros.put("ptrncod", request.getTrnCodigo());
        parametros.put("pesquema", esquema);
        parametros.put("pathfondo", pathFondo);
        parametros.put("pexceso", request.getPexceso());
        parametros.put("pvconsumo", request.getPvconsumo());
        parametros.put("pvexceso", request.getPvexceso());
        parametros.put("pvsubt", request.getPvsubt());
        parametros.put("pvdesc", request.getPvdesc());
        parametros.put("pvmulta", request.getPvmulta());
        parametros.put("pvtotal", request.getPvtotal());
        parametros.put("pfechamaxpago", request.getPfechamaxpago());

        byte[] reportContent = jasperReportService.runPdfReport(pathReporte, parametros);

        String reportName = String.format("ComprobanteAgua_%d.pdf", request.getTrnCodigo());
        return buildResponse(reportName, reportContent);
    }
}
