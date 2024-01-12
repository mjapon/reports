package com.mavil.reports.service;

import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Map;

@Service
public class JasperReportService {

    @Autowired
    private DataSource dataSource;

    public byte[] runPdfReport(String reportPath, Map parameters) throws FileNotFoundException, JRException, SQLException {
        File file = ResourceUtils.getFile(reportPath);
        JasperReport jasperReport
                = JasperCompileManager.compileReport(file.getAbsolutePath());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

}
