package com.mavil.reports.service;


import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@UtilityClass
@Slf4j
public class ExcelUtilService {

    public static final Integer DEFAULT_CELL_WIDTH = 27 * 256;

    public static void addCell(String label, Row row, Integer columIndex, CellStyle cellStyle) {
        Cell cell = row.createCell(columIndex);
        cell.setCellValue(label);
        cell.setCellStyle(cellStyle);
    }

    public static CellStyle createCellStyle(XSSFWorkbook workbook, Short backgroundColor, Short fontColor, Boolean bold) {
        return createCellStyle(workbook, backgroundColor, fontColor, bold, HorizontalAlignment.CENTER);
    }

    public static CellStyle createCellStyle(XSSFWorkbook workbook, Short backgroundColor, Short fontColor,
                                            Boolean bold, HorizontalAlignment horizontalAlignment) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setBold(bold);
        font.setColor(fontColor);
        style.setFillForegroundColor(backgroundColor);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setWrapText(true);
        style.setAlignment(horizontalAlignment);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(font);
        return style;
    }

}
