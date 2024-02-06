package com.mavil.reports.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.parsers.ParserConfigurationException;
import java.sql.SQLException;

@RestController
@RequestMapping("/ride")
public class ReportRideController extends ReportRideBaseController {

    @GetMapping("{claveacceso}")
    public ResponseEntity<ByteArrayResource> getRideReport(@PathVariable String claveacceso, @RequestParam String fmt) throws SQLException, ParserConfigurationException {

        if ("pdf".equals(fmt)) {
            return genPdfRide(claveacceso);
        } else {
            return genXmlRide(claveacceso);
        }
    }

}
