package com.mavil.reports.service;

import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class JasperReportService {

    @Autowired
    private DataSource dataSource;

    public byte[] getItemReport(String ticketId) throws FileNotFoundException, JRException, SQLException {

        File file = ResourceUtils.getFile("/Users/manueljapon/JaspersoftWorkspace/MyReports/ticket2.jrxml");
        JasperReport jasperReport
                = JasperCompileManager.compileReport(file.getAbsolutePath());

        Map parametros = new HashMap();
        parametros.put("ticketid", Integer.valueOf(ticketId));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource.getConnection());

        byte[] reportContent = JasperExportManager.exportReportToPdf(jasperPrint);

        return reportContent;

    }
}
