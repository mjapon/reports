package com.mavil.reports.controller;

import com.mavil.reports.repository.TAttachRepository;
import com.mavil.reports.repository.TParamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/getAttach")
@Slf4j
public class FileRxDocController extends ReportBaseController {
    @Autowired
    private TAttachRepository attachRepository;

    private static final String RXD_TYPE = "rxd";

    @GetMapping("get")
    public ResponseEntity<ByteArrayResource> getReport(@RequestParam Integer emp, @RequestParam String cod, @RequestParam String tipo) {

        String empEsquema = getEmpEsquema(emp);

        String strRuta = String.format("%s_ruta", tipo);
        String strExt = String.format("%s_ext", tipo);
        String strFilename = String.format("%s_filename", tipo);

        Map<String, String> datosDocMap;

        if (RXD_TYPE.equals(tipo)) {
            datosDocMap = attachRepository.getDatosDocRx(empEsquema, cod);
        } else {
            datosDocMap = attachRepository.getDatosDocAttach(empEsquema, cod);
        }

        if (datosDocMap != null) {
            String rutaDbFile = datosDocMap.get(strRuta);
            String extDbFile = datosDocMap.get(strExt);
            String nameDbFile = datosDocMap.get(strFilename);

            try {
                FileInputStream fileInputStream = new FileInputStream(rutaDbFile);

                return buildCustomTypeResponse(nameDbFile, extDbFile, fileInputStream.readAllBytes());
            } catch (FileNotFoundException e) {
                log.error("Error al tratar de obtener archivo adjunto", e);
            } catch (IOException e) {
                log.error("Error al leer archivo archivo adjunto", e);
            }
        }

        return buildHtmlMessage("<h1>No pude recuperar los datos del archivo</h1>");

    }
}
