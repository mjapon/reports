package com.mavil.reports.service;

import com.mavil.reports.vo.FilaBalanceVo;
import com.mavil.reports.vo.GenBalanceRequestVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ExcelReporteGenService {

    public byte[] generateExcel(GenBalanceRequestVo requestVo) {

        byte[] bytes = null;

        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            XSSFSheet sheet = workbook.createSheet();

            //codigo de barras
            CellStyle centerStyle = ExcelUtilService.createCellStyle(workbook, IndexedColors.WHITE.getIndex(),
                    IndexedColors.BLACK.getIndex(), false, HorizontalAlignment.CENTER);

            CellStyle rightStyle = ExcelUtilService.createCellStyle(workbook, IndexedColors.WHITE.getIndex(),
                    IndexedColors.BLACK.getIndex(), false, HorizontalAlignment.RIGHT);

            CellStyle blueStyle = ExcelUtilService.createCellStyle(workbook, IndexedColors.MAROON.getIndex(),
                    IndexedColors.WHITE.getIndex(), true, HorizontalAlignment.LEFT);
            CellStyle blankStyle = ExcelUtilService.createCellStyle(workbook, IndexedColors.WHITE.getIndex(),
                    IndexedColors.BLACK.getIndex(), false, HorizontalAlignment.LEFT);

            List<String> blueColumns = new ArrayList<>(Arrays.asList("Código", "Nombre", "Saldo"));

            Row row0 = sheet.createRow(0);

            Cell cell = row0.createCell(0);
            cell.setCellValue(String.format("%s\nPeriodo:%s",requestVo.getTitulo(), requestVo.getPeriodo()));
            cell.setCellStyle(centerStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

            Row row = sheet.createRow(1);
            int columnCount = 0;
            for (String column : blueColumns) {
                ExcelUtilService.addCell(column, row, columnCount + blueColumns.indexOf(column), blueStyle);
                sheet.setColumnWidth(columnCount + blueColumns.indexOf(column), ExcelUtilService.DEFAULT_CELL_WIDTH);
            }

            sheet.setColumnWidth(1, ExcelUtilService.DEFAULT_CELL_WIDTH * 3);

            List<FilaBalanceVo> items = requestVo.getItems();

            for (FilaBalanceVo rowData : items) {
                Row rowIt = sheet.createRow(items.indexOf(rowData) + 2);
                ExcelUtilService.addCell(rowData.getCodigo(), rowIt, 0, blankStyle);
                ExcelUtilService.addCell(rowData.getNombre(), rowIt, 1, blankStyle);
                ExcelUtilService.addCell(rowData.getTotal(), rowIt, 2, rightStyle);
            }

            workbook.write(outputStream);
            bytes = outputStream.toByteArray();
            outputStream.flush();
        } catch (Exception ex) {
            log.error("Error to download localheadtask items file: ", ex);
        }
        return bytes;

    }
}
