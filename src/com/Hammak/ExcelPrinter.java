package com.Hammak;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class ExcelPrinter {

    private static final int DAY_TITLE_COLUMN = 2;
    private static final int ONE_SYMBOL_COLUMN_WIDTH = 750;

    ExcelPrinter() {
    }

    void printSemester(Semester semester, String filename) throws IOException {
        Workbook wb = new XSSFWorkbook();

        for (int i = 0; i < semester.getWeeksAmount(); i++) {
            if (!semester.getWeek(i).isEmpty()) {
                printWeek(semester.getWeek(i), wb);
            }
        }

        try (OutputStream fileOut = new FileOutputStream(filename)) {
            wb.write(fileOut);
        }
    }

    private void printWeek(Week week, Workbook wb) {
        Sheet sheet = wb.createSheet(getWeekTitleString(week));
        int currentRow = 0;
        for (int i = 0; i < week.daysAmount(); i++) {
            if (!week.getDay(i).isEmpty()) {
                currentRow = printDay( currentRow, wb, sheet, week.getDay(i));
            }
        }
    }

    public static String getWeekTitleString(Week week) {
        return String.format("%02d.%02d", week.getDay(0).getDate().getDayOfMonth(),
                week.getDay(0).getDate().getMonthValue());
    }

    private int printDay(int currentRow, Workbook wb, Sheet sheet, Day day) {
        createDayTitleCell( currentRow, wb, sheet, day);
        currentRow++;

        for (int i = 0; i < day.pairsAmount(); i++) {
            if (!day.getPair(i).isEmpty()) {
                printPair(day.getPair(i), currentRow, wb, sheet);
                currentRow++;
            }
        }

        return currentRow;
    }

    private void createDayTitleCell(int currentRow, Workbook wb, Sheet sheet, Day day) {
        Row row = sheet.createRow(currentRow);
        Cell cell = row.createCell(DAY_TITLE_COLUMN);

        for (int i = 0; i < 6; i++) {
            Cell cellIter = row.createCell(i);
            setColor(wb, cellIter);

            if (currentRow == 0) {
                CellStyle style = cellIter.getCellStyle();
                style.setBorderTop(XSSFCellStyle.BORDER_THIN);

                cellIter.setCellStyle(style);
            }
            if (i == 0) {
                CellStyle style = cellIter.getCellStyle();
                style.setBorderLeft(XSSFCellStyle.BORDER_THIN);

                cellIter.setCellStyle(style);
            }
            if (i == 5) {
                CellStyle style = cellIter.getCellStyle();
                style.setBorderRight(XSSFCellStyle.BORDER_THIN);

                cellIter.setCellStyle(style);
            }
        }

        cell.setCellValue(getdayTitle(day));

        setAllignCenterCellStyle(wb, cell);
        setColor(wb, cell);
        if (currentRow == 0) {
            CellStyle style = cell.getCellStyle();
            style.setBorderTop(XSSFCellStyle.BORDER_THIN);

            cell.setCellStyle(style);
        }
    }

    private void printPair(Pair pair, int currentRow, Workbook wb, Sheet sheet) {

        Row row = sheet.createRow(currentRow);

        createNumberCell(pair, wb, row);
        createTimeCell(pair, wb, row);
        createSubjectCell(pair, wb, row);
        createTypeCell(pair, wb, row);
        createTeacherCell(pair, wb, row);
        createLectureHallNumberCell(pair, wb, row);

        setColumnWidths(sheet);
    }

    private void setColumnWidths(Sheet sheet) {
        sheet.setColumnWidth(0, ONE_SYMBOL_COLUMN_WIDTH);
        sheet.setColumnWidth(3, ONE_SYMBOL_COLUMN_WIDTH);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(4);
    }

    private void createLectureHallNumberCell(Pair pair, Workbook wb, Row row) {
        Cell cell = row.createCell(5);
        cell.setCellValue(pair.getLectureHallNumber());

        setAllignCenterCellStyle(wb, cell);

        setAllBorders(cell, wb);
    }

    private void createTeacherCell(Pair pair, Workbook wb, Row row) {
        Cell cell = row.createCell(4);
        cell.setCellValue(pair.getTeacher());

        setAllBorders(cell, wb);
    }

    private void createTypeCell(Pair pair, Workbook wb, Row row) {
        Cell cell = row.createCell(3);
        cell.setCellValue(Character.toString(pair.getType()));

        setAllignCenterCellStyle(wb, cell);

        setAllBorders(cell, wb);
    }

    private void createSubjectCell(Pair pair, Workbook wb, Row row) {
        Cell cell = row.createCell(2);

        cell.setCellValue(pair.getSubject());

        setAllBorders(cell, wb);
    }

    private void createTimeCell(Pair pair, Workbook wb, Row row) {
        Cell cell = row.createCell(1);
        // 09:00-10:20
        cell.setCellValue(String.format("%02d:%02d-%02d:%02d", pair.getStartTime().getHour(),
                pair.getStartTime().getMinute(),
                pair.getStartTime().plusMinutes(80).getHour(),
                pair.getStartTime().plusMinutes(80).getMinute()));

        setAllBorders(cell, wb);
        setColor(wb, cell);
    }

    private void createNumberCell(Pair pair, Workbook wb, Row row) {
        Cell cell = row.createCell(0);
        cell.setCellValue(pair.getNumber());

        setAllignCenterCellStyle(wb, cell);
        setAllBorders(cell, wb);
        setColor(wb, cell);
    }

    private void setColor(Workbook wb, Cell cell) {
        XSSFCellStyle style;
        if (cell.getCellStyle().getIndex() == 0) {
            style = (XSSFCellStyle) wb.createCellStyle();
        } else {
            style = (XSSFCellStyle) cell.getCellStyle();
        }

        XSSFColor myColor = new XSSFColor(Color.decode("#ffd966"));
        style.setFillForegroundColor(myColor);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cell.setCellStyle(style);
    }

    private void setAllBorders(Cell cell, Workbook wb) {
        CellStyle style;
        if (cell.getCellStyle().getIndex() == 0) {
            style = wb.createCellStyle();
        } else {
            style = cell.getCellStyle();
        }

        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cell.setCellStyle(style);
    }

    private void setAllignCenterCellStyle(Workbook wb, Cell cell) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cell.setCellStyle(cellStyle);
    }

    private String getdayTitle(Day day) {
        return String.format("%s %02d.%02d", day.getStringName(),
                day.getDate().getDayOfMonth(),
                day.getDate().getMonthValue());
    }

}
