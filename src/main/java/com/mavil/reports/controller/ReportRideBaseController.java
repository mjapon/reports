package com.mavil.reports.controller;

import com.mavil.reports.repository.TDatosFacteRepository;
import com.mavil.reports.repository.TParamRepository;
import com.mavil.reports.service.JasperReportService;
import com.mavil.reports.util.GenXmlCompeUtil;
import com.mavil.reports.vo.DatosAsiFacteVo;
import com.mavil.reports.vo.DatosFacteVo;
import com.mavil.reports.vo.TransaccDataVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ReportRideBaseController extends ReportBaseController {

    @Autowired
    private TDatosFacteRepository datosFacteRepository;

    @Autowired
    private JasperReportService jasperReportService;

    @Autowired
    private TParamRepository paramRepository;

    private String getRucEmpresa(String claveAcceso) {
        return claveAcceso.substring(10, 23);
    }

    private ResponseEntity<ByteArrayResource> genEmptyResponse(){
        return buildHtmlMessage("Se produjo un error al tratar de generar RIDE, no hay datos de factura o configuracion de empresa");
    }
    protected ResponseEntity<ByteArrayResource> genPdfRide(String claveacceso) throws SQLException {

        Optional<DatosFacteVo> datosFacteVo = datosFacteRepository.getDatosFacte(getRucEmpresa(claveacceso));

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

                Map reportParams = new HashMap();
                reportParams.put("ptrncod", Integer.valueOf(datosAsiFacteVo.get().getTrncodigo()));
                reportParams.put("pesquema", esquema);
                reportParams.put("pathlogo", pathLogo);

                byte[] reportContent = jasperReportService.runPdfReport(pathRideTemplate, reportParams);

                String reportName = String.format("RIDE_%s.pdf", datosAsiFacteVo.get().getTrncodigo());
                return buildPDFResponse(reportName, reportContent);
            }
        }
        return genEmptyResponse();
    }

    protected ResponseEntity<ByteArrayResource> genXmlRide(String claveacceso) throws SQLException, ParserConfigurationException {

        Optional<DatosFacteVo> datosFacteVo = datosFacteRepository.getDatosFacte(getRucEmpresa(claveacceso));
        if (datosFacteVo.isPresent()) {
            Optional<DatosAsiFacteVo> datosAsiFacteVo = datosFacteRepository.getDatosAsiFacte(datosFacteVo.get().getEsquemaDb(), claveacceso);

            if (datosAsiFacteVo.isPresent()) {
                String pathDocSigned = new StringBuilder(datosFacteVo.get().getPathDocsSigned())
                        .append(File.separatorChar).append(claveacceso).append(".xml").toString();
                GenXmlCompeUtil genXmlCompeUtil = new GenXmlCompeUtil();
                String xml = genXmlCompeUtil.genXml(datosAsiFacteVo.get().getEstado(),
                        datosAsiFacteVo.get().getNumAutorizacion(),
                        datosAsiFacteVo.get().getFechaAutorizacion(),
                        datosAsiFacteVo.get().getAmbiente(),
                        pathDocSigned);

                return buildXmlMessage(xml);
            }
        }

        return genEmptyResponse();

    }


}
