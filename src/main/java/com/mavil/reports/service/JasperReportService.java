package com.mavil.reports.service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class JasperReportService {

    @Autowired
    private DataSource dataSource;

    public byte[] getItemReport() throws FileNotFoundException, JRException, SQLException {


        InputStream employeeReportStream
                = getClass().getResourceAsStream("/employeeReport.jrxml");
        JasperReport jasperReport
                = JasperCompileManager.compileReport(employeeReportStream);

        //File file = getClass().getResourceAsStream("item-report.jrxml");
//        ResourceUtils.getFile("classpath:item-report.jrxml");
        //JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        Map parametros = new HashMap();
        parametros.put("ptrncod", Integer.valueOf("1"));
        parametros.put("pesquema", "esquema");
        parametros.put("pathfondo", "pathFondo");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource.getConnection());

        /*JRPdfExporter exporter = new JRPdfExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(
                new SimpleOutputStreamExporterOutput("employeeReport.pdf"));

        SimplePdfReportConfiguration reportConfig
                = new SimplePdfReportConfiguration();
        reportConfig.setSizePageToContent(true);
        reportConfig.setForceLineBreakPolicy(false);

        SimplePdfExporterConfiguration exportConfig
                = new SimplePdfExporterConfiguration();
        exportConfig.setMetadataAuthor("baeldung");
        exportConfig.setEncrypted(true);
        exportConfig.setAllowedPermissionsHint("PRINTING");

        exporter.setConfiguration(reportConfig);
        exporter.setConfiguration(exportConfig);

        exporter.exportReport();

         */

        byte[] reportContent = JasperExportManager.exportReportToPdf(jasperPrint);

        return reportContent;

    }
}
