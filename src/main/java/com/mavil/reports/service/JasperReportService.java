package com.mavil.reports.service;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.web.util.WebHtmlResourceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@Service
@Slf4j
public class JasperReportService {

    private static String JASPER_TYPE_FILE = ".jasper";
    private static String JRXML_TYPE_FILE = ".jrxml";

    @Autowired
    private DataSource dataSource;

    public byte[] runPdfReport(String reportPath, Map parameters) throws SQLException {

        Long timea = System.currentTimeMillis();
        byte[] result = {};
        Connection connection = dataSource.getConnection();
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(getJasperPrint(reportPath), parameters, connection);
            result = JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Throwable ex) {
            log.error(String.format("Error al tratar de ejecutar reporte %s", reportPath), ex);
        } finally {
            try {
                connection.close();
            } catch (Throwable e) {
                log.error("Error al tratar de cerrar la conexion de base de datos");
            }
        }
        Long timeb = System.currentTimeMillis();
        log.info(String.format("Tiempo: %d", timeb - timea));
        return result;
    }


    public byte[] runExcelReport(String reportPath, Map parameters) throws SQLException {

        Long timea = System.currentTimeMillis();
        byte[] result = {};
        Connection connection = dataSource.getConnection();
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(getJasperPrint(reportPath), parameters, connection);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            JRXlsExporter exporter = new JRXlsExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint)); //The JasperPrint, filled report
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos)); //Your ByteArrayOutputStream

            SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
            configuration.setOnePagePerSheet(true);
            configuration.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
            configuration.setRemoveEmptySpaceBetweenColumns(Boolean.TRUE);
            configuration.setCellHidden(Boolean.FALSE);
            configuration.setDetectCellType(true);
            configuration.setDetectCellType(true);
            exporter.setConfiguration(configuration);

            exporter.exportReport();
            result = baos.toByteArray();
        } catch (Throwable ex) {
            log.error(String.format("Error al tratar de ejecutar reporte %s", reportPath, ex));
        } finally {
            try {
                connection.close();
            } catch (Throwable e) {
                log.error("Error al tratar de cerrar la conexion de base de datos");
            }
        }
        Long timeb = System.currentTimeMillis();
        log.info(String.format("Tiempo: %d", timeb - timea));
        return result;
    }

    public byte[] runHtmlReport(String reportPath, Map parameters) throws SQLException {

        Long timea = System.currentTimeMillis();
        byte[] result = {};
        Connection connection = dataSource.getConnection();
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(getJasperPrint(reportPath), parameters, connection);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            HtmlExporter exporter = new HtmlExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            SimpleHtmlExporterOutput output = new SimpleHtmlExporterOutput(baos);
            output.setImageHandler(new WebHtmlResourceHandler("image?image={0}"));
            exporter.setExporterOutput(output);
            exporter.exportReport();

            result = baos.toByteArray();
        } catch (Throwable ex) {
            log.error(String.format("Error al tratar de ejecutar reporte %s", reportPath, ex));
        } finally {
            try {
                connection.close();
            } catch (Throwable e) {
                log.error("Error al tratar de cerrar la conexion de base de datos");
            }
        }
        Long timeb = System.currentTimeMillis();
        log.info(String.format("Tiempo: %d", timeb - timea));
        return result;
    }

    private JasperReport getJasperPrint(String reportPath) throws JRException, FileNotFoundException {
        if (reportPath.endsWith(JASPER_TYPE_FILE)) {
            return getCompiledReport(reportPath);
        } else {
            String compiledReportPath = reportPath.substring(0, reportPath.lastIndexOf(JRXML_TYPE_FILE)).concat(JASPER_TYPE_FILE);
            try {
                return getCompiledReport(compiledReportPath);
            } catch (JRException e) {
                return compileReport(reportPath);
            }
        }
    }

    private JasperReport getCompiledReport(String jasperPath) throws JRException {
        return (JasperReport) JRLoader.loadObject(new File(jasperPath));
    }

    private JasperReport compileReport(String jrxmlPath) throws FileNotFoundException, JRException {
        File file = ResourceUtils.getFile(jrxmlPath);
        return JasperCompileManager.compileReport(file.getAbsolutePath());

    }


}
