package com.mavil.reports.controller;

import com.mavil.reports.repository.TDatosFacteRepository;
import com.mavil.reports.repository.TParamRepository;
import com.mavil.reports.service.JasperReportService;
import com.mavil.reports.util.GenXmlCompeUtil;
import com.mavil.reports.vo.DatosAsiFacteVo;
import com.mavil.reports.vo.DatosFacteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.Optional;

@RestController
@RequestMapping("/xmlCompe")
public class XmlRideController extends ReportBaseController {

    @Autowired
    private JasperReportService jasperReportService;

    @Autowired
    private TParamRepository paramRepository;

    @Autowired
    private TDatosFacteRepository datosFacteRepository;

    @GetMapping("get")
    public ResponseEntity<ByteArrayResource> getXmlRideReport(@RequestParam String claveacceso) throws ParserConfigurationException {

        String rucEmpresa = claveacceso.substring(10, 23);

        Optional<DatosFacteVo> datosFacteVo = datosFacteRepository.getDatosFacte(rucEmpresa);
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

        return buildXmlMessage("<doc>Se produjo un error al tratar de generar RIDE, no hay datos de factura o configuracion de empresa</doc>");
    }

}
