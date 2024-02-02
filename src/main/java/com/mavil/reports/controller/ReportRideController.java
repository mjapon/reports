package com.mavil.reports.controller;

import com.mavil.reports.repository.TDatosFacteRepository;
import com.mavil.reports.repository.TParamRepository;
import com.mavil.reports.service.JasperReportService;
import com.mavil.reports.vo.DatosAsiFacteVo;
import com.mavil.reports.vo.DatosFacteVo;
import com.mavil.reports.vo.TransaccDataVo;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Optional;

@RestController
@RequestMapping("/ride")
public class ReportRideController extends ReportBaseController {

    @Autowired
    private JasperReportService jasperReportService;

    @Autowired
    private TParamRepository paramRepository;

    @Autowired
    private TDatosFacteRepository datosFacteRepository;

    @GetMapping("get")
    public ResponseEntity<ByteArrayResource> getRideReport(@RequestParam String claveacceso) throws SQLException {
        String rucEmpresa = claveacceso.substring(10, 23);

        Optional<DatosFacteVo> datosFacteVo = datosFacteRepository.getDatosFacte(rucEmpresa);

        if (datosFacteVo.isPresent()) {
            String esquema = datosFacteVo.get().getEsquemaDb();
            Optional<DatosAsiFacteVo> datosAsiFacteVo = datosFacteRepository.getDatosAsiFacte(esquema, claveacceso);

            if (datosAsiFacteVo.isPresent()) {
                TransaccDataVo transaccDataVo = paramRepository.getTransaccData(esquema, Integer.valueOf(datosAsiFacteVo.get().getTrncodigo()));

                String pathRideTemplate = paramRepository.getParamValue(esquema, "pathReporteRide", transaccDataVo.getSecCodigo());

                if (StringUtils.isEmpty(pathRideTemplate)) {
                    pathRideTemplate = "/opt/reportes/ride_factura.jrxml";
                }

                String pathLogo = datosFacteVo.get().getPathLogo();
                if (StringUtils.isEmpty(pathLogo) || ("null".equals(pathLogo.trim()))) {
                    pathLogo = "/opt/reportes/imgs/logosfacte/MavilCuadro.png";
                }

                Map parametros = new HashMap();
                parametros.put("ptrncod", Integer.valueOf(datosAsiFacteVo.get().getTrncodigo()));
                parametros.put("pesquema", esquema);
                parametros.put("pathlogo", pathLogo);

                byte[] reportContent = jasperReportService.runPdfReport(pathRideTemplate, parametros);

                String reportName = String.format("RIDE_%s.pdf", datosAsiFacteVo.get().getTrncodigo());
                return buildPDFResponse(reportName, reportContent);

            }


        }

        return buildHtmlMessage("Se produjo un error al tratar de generar RIDE, no hay datos de factura o configuracion de empresa");

    }

}
