package com.mavil.reports.controller;

import com.mavil.reports.repository.TParamRepository;
import com.mavil.reports.repository.TReportGenRepository;
import com.mavil.reports.vo.ReportGenParamsVo;
import com.mavil.reports.vo.TReportVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/reporteGen")
public class ReportGenController extends ReportBaseController {

    @Autowired
    private TParamRepository paramRepository;

    @Autowired
    private TReportGenRepository reportGenRepository;

    @PostMapping("gen")
    public ResponseEntity<ByteArrayResource> getReport(@RequestBody ReportGenParamsVo request) throws SQLException {

        String esquema = this.getEmpEsquema(request.getCodemp());
        TReportVo tReportVo = reportGenRepository.getDatosReporte(request.getCodrep(), esquema);


        Map parametros = new HashMap();
        parametros.put("pesquema", esquema);
        parametros.put("pdesde", request.getPdesde());
        parametros.put("phasta", request.getPhasta());
        parametros.put("psecid", request.getPsecid());
        parametros.put("pusid", request.getPusid());
        parametros.put("prefid", request.getPrefid());
        String pfechas = String.format("and asi.trn_fecreg between '%s' and '%s'", request.getPdesde(), request.getPhasta());
        parametros.put("pfechas", pfechas);
        parametros.put("labelparams", request.getLabelParams());


        String formato = request.getPfmt();
        String pathReporte = tReportVo.getRepJasper();


        //String contentType = "application/pdf;";
        String inline = "inline";
        String ext = "pdf";
        if ("2".equalsIgnoreCase(formato)) {
            //contentType = "application/vnd.ms-excel;";
            ext = "xls";
            inline = "attachment";
        } else if ("3".equalsIgnoreCase(formato)) {
            //contentType = "text/html;";
            ext = ".html";
        }

        String fechahora = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String filename = String.format("Reporte%s_%s.%s", tReportVo.getRepNombre(), fechahora, ext);
        //String contentDisposition = String.format("%s; filename=%s", inline, filename);


        byte[] reportContent;
        if ("2".equalsIgnoreCase(formato)) {
            reportContent = jasperReportService.runExcelReport(pathReporte, parametros);
        } else if ("3".equalsIgnoreCase(formato)) {
            reportContent = jasperReportService.runHtmlReport(pathReporte, parametros);
        } else {
            reportContent = jasperReportService.runPdfReport(pathReporte, parametros);
        }

        return buildCustomTypeResponse(filename, ext, reportContent);

    }
}
