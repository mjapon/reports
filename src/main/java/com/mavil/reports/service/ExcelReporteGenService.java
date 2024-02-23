package com.mavil.reports.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ExcelReporteGenService {

    public static byte[] generateExcel() {

        byte[] bytes = null;

        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            XSSFSheet sheet = workbook.createSheet();


            //codigo de barras
            CellStyle blueStyle = ExcelUtilService.createCellStyle(workbook, IndexedColors.RED.getIndex(),
                    IndexedColors.WHITE.getIndex(), true);
            CellStyle blankStyle = ExcelUtilService.createCellStyle(workbook, IndexedColors.WHITE.getIndex(),
                    IndexedColors.BLACK.getIndex(), false);

            List<String> blueColumns = new ArrayList<>(Arrays.asList("Nro", "Local", "Artículos", "Estado", "Progreso"));

            Row row0 = sheet.createRow(0);

            Cell cell = row0.createCell(0);
            cell.setCellValue("REPORTE DE TAREAS");
            cell.setCellStyle(blankStyle);
            sheet.addMergedRegion(new CellRangeAddress(0,0,0,5));

            Row row = sheet.createRow(1);
            int columnCount = 0;
            for (String column : blueColumns) {
                ExcelUtilService.addCell(column, row, columnCount + blueColumns.indexOf(column), blueStyle);
                sheet.setColumnWidth(columnCount + blueColumns.indexOf(column), ExcelUtilService.DEFAULT_CELL_WIDTH);
            }

            /*
            for (LocalHeadTaskVo rowData : localHeadTasks) {
                Row rowIt = sheet.createRow(localHeadTasks.indexOf(rowData) + 2);
                ExcelPoiUtil.addCell(String.valueOf(localHeadTasks.indexOf(rowData) + 2), rowIt, 0, blankStyle);
                ExcelPoiUtil.addCell(String.format("%d - %s - %s",
                        rowData.getWorkAreaReferenceCode(),
                        rowData.getWorkAreaBusinessFormat(),
                        rowData.getWorkAreaName()), rowIt, 1, blankStyle);
                ExcelPoiUtil.addCell(String.valueOf(rowData.getNumTasks()), rowIt, 2, blankStyle);
                ExcelPoiUtil.addCell(rowData.getStatusName(), rowIt, 3, blankStyle);
                ExcelPoiUtil.addCell(String.valueOf(rowData.getProgress()), rowIt, 4, blankStyle);
            }*/

            workbook.write(outputStream);
            bytes = outputStream.toByteArray();
            outputStream.flush();
        } catch (Exception ex) {
            log.error("Error to download localheadtask items file: ", ex);
        }
        return bytes;

    }
}
