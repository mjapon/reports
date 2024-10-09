package com.mavil.reports.controller;

import com.mavil.reports.repository.TDatosFacteRepository;
import com.mavil.reports.repository.TParamRepository;
import com.mavil.reports.service.JasperReportService;
import com.mavil.reports.util.GenXmlCompeUtil;
import com.mavil.reports.vo.DatosAsiFacteVo;
import com.mavil.reports.vo.DatosFacteVo;
import com.mavil.reports.vo.NotaCredInfoVo;
import com.mavil.reports.vo.TransaccDataVo;
import org.apache.commons.lang3.ObjectUtils;
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

    private static final String TRA_COD_NOTACRED = "4";

    private String getRucEmpresa(String claveAcceso) {
        return claveAcceso.substring(10, 23);
    }

    private ResponseEntity<ByteArrayResource> genEmptyResponse() {
        return buildHtmlMessage("Se produjo un error al tratar de generar RIDE, no hay datos de factura o configuracion de empresa");
    }

    private Boolean isNotaCred(TransaccDataVo transaccDataVo) {
        if (transaccDataVo != null && StringUtils.isNotEmpty(transaccDataVo.getTraCodigo())) {
            return transaccDataVo.getTraCodigo().equals(TRA_COD_NOTACRED);
        }
        return false;
    }

    private String getPathRideFromTransaccType(TransaccDataVo transaccDataVo, String esquema) {
        String pathReporteRide = "/opt/reportes/ride_factura.jrxml";
        String auxPathRideTemplate;
        if (isNotaCred(transaccDataVo)) {
            auxPathRideTemplate = paramRepository.getParamValue(esquema, "pathRepRideNotaCred", transaccDataVo.getSecCodigo());
        } else {
            auxPathRideTemplate = paramRepository.getParamValue(esquema, "pathReporteRide", transaccDataVo.getSecCodigo());
        }
        if (StringUtils.isNotEmpty(auxPathRideTemplate)) {
            pathReporteRide = auxPathRideTemplate;
        }
        return pathReporteRide;
    }

    protected ResponseEntity<ByteArrayResource> genPdfRide(String claveacceso) throws SQLException {

        Optional<DatosFacteVo> datosFacteVo = datosFacteRepository.getDatosFacte(getRucEmpresa(claveacceso));

        if (datosFacteVo.isPresent()) {
            String esquema = datosFacteVo.get().getEsquemaDb();
            Optional<DatosAsiFacteVo> datosAsiFacteVo = datosFacteRepository.getDatosAsiFacte(esquema, claveacceso);

            if (datosAsiFacteVo.isPresent()) {
                Integer trnCodigo = Integer.valueOf(datosAsiFacteVo.get().getTrncodigo());
                TransaccDataVo transaccDataVo = paramRepository.getTransaccData(esquema, trnCodigo);

                String pathRideTemplate = getPathRideFromTransaccType(transaccDataVo, esquema);

                String pathLogo = datosFacteVo.get().getPathLogo();
                if (StringUtils.isEmpty(pathLogo) || ("null".equals(pathLogo.trim()))) {
                    pathLogo = "/opt/reportes/imgs/logosfacte/MavilCuadro.png";
                }

                Map<String, Object> reportParams = new HashMap<>();
                reportParams.put("ptrncod", Integer.valueOf(datosAsiFacteVo.get().getTrncodigo()));
                reportParams.put("pesquema", esquema);
                reportParams.put("pathlogo", pathLogo);

                if (isNotaCred(transaccDataVo)) {
                    NotaCredInfoVo datosNotaCred = paramRepository.getDatosNotaCred(esquema, trnCodigo);
                    if (datosNotaCred != null) {
                        reportParams.put("numerofactura", datosNotaCred.getNumeroFactura());
                        reportParams.put("fechafactura", datosNotaCred.getFechaFactura());
                    }
                }

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
