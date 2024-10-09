package com.mavil.reports.service;

import com.mavil.reports.vo.ColumnReportVo;
import com.mavil.reports.vo.GenerateReportRequestVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TGridService {

    public byte[] generateExcel(GenerateReportRequestVo requestVo) {

        byte[] bytes = null;

        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            XSSFSheet sheet = workbook.createSheet();

            CellStyle centerStyle = ExcelUtilService.createCellStyle(workbook, IndexedColors.WHITE.getIndex(),
                    IndexedColors.BLACK.getIndex(), false, HorizontalAlignment.CENTER);

            CellStyle blueStyle = ExcelUtilService.createCellStyle(workbook, IndexedColors.MAROON.getIndex(),
                    IndexedColors.WHITE.getIndex(), true, HorizontalAlignment.LEFT);

            CellStyle blankStyle = ExcelUtilService.createCellStyle(workbook, IndexedColors.WHITE.getIndex(),
                    IndexedColors.BLACK.getIndex(), false, HorizontalAlignment.LEFT);

            Row row0 = sheet.createRow(0);
            List<ColumnReportVo> columns = requestVo.getColumns();
            int columnNumbers = columns.size();

            Cell cell = row0.createCell(0);
            cell.setCellValue(requestVo.getTitle());
            cell.setCellStyle(centerStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnNumbers));

            Row row = sheet.createRow(1);
            for (ColumnReportVo column : columns) {
                ExcelUtilService.addCell(column.getLabel(), row, columns.indexOf(column), blueStyle);
                sheet.setColumnWidth(columns.indexOf(column), ExcelUtilService.DEFAULT_CELL_WIDTH);
            }

            List<Map<String, String>> items = requestVo.getData();

            for (Map<String, String> rowData : items) {
                Row rowIt = sheet.createRow(items.indexOf(rowData) + 2);
                for (ColumnReportVo column : columns) {
                    String columnValue = rowData.getOrDefault(column.getField(), "");
                    ExcelUtilService.addCell(columnValue, rowIt, columns.indexOf(column), blankStyle);
                }
            }
            
            if (!ObjectUtils.isEmpty(requestVo.getTotals())) {
                Row rowIt = sheet.createRow(items.size() + 3);
                for (ColumnReportVo column : requestVo.getColumns()) {
                    String totalColumValue = requestVo.getTotals().getOrDefault(column.getField(), "");
                    ExcelUtilService.addCell(totalColumValue, rowIt, requestVo.getColumns().indexOf(column), blankStyle);
                }
            }

            workbook.write(outputStream);
            bytes = outputStream.toByteArray();
            outputStream.flush();
        } catch (Exception ex) {
            log.error("Error to download gridreport excel file: ", ex);
        }
        return bytes;

    }

}
