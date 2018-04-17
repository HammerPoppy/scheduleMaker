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

        for (int i = 0; i < semester.weeksAmount(); i++) {
            if (!semester.getWeek(i).isEmpty()) {
                printWeek(i, wb, semester);
            }
        }

        try (OutputStream fileOut = new FileOutputStream(filename)) {
            wb.write(fileOut);
        }
    }

    private void printWeek(int weekIndex, Workbook wb, Semester semester) {
        Sheet sheet = wb.createSheet(getWeekTitleString(weekIndex, semester));
        int currentRow = 0;
        for (int i = 0; i < semester.getWeek(weekIndex).daysAmount(); i++) {
            if (!semester.getWeek(weekIndex).getDay(i).isEmpty()) {
                currentRow = printDay(i, weekIndex, currentRow, wb, sheet, semester);
            }
        }
    }

    private String getWeekTitleString(int weekIndex, Semester semester) {
        return String.format("%02d.%02d", semester.getWeek(weekIndex).getDay(0).getDate().getDayOfMonth(),
                semester.getWeek(weekIndex).getDay(0).getDate().getMonthValue());
    }

    private int printDay(int dayIndex, int weekIndex, int currentRow, Workbook wb, Sheet sheet, Semester semester) {
        createDayTitleCell(dayIndex, weekIndex, currentRow, wb, sheet, semester);
        currentRow++;

        for (int i = 0; i < semester.getWeek(weekIndex).getDay(dayIndex).pairsAmount(); i++) {
            if (!semester.getWeek(weekIndex).getDay(dayIndex).getPair(i).isEmpty()) {
                printPair(i, dayIndex, weekIndex, currentRow, wb, sheet, semester);
                currentRow++;
            }
        }

        return currentRow;
    }

    private void createDayTitleCell(int dayIndex, int weekIndex, int currentRow, Workbook wb, Sheet sheet, Semester semester) {
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

        cell.setCellValue(getdayTitle(dayIndex, weekIndex, semester));

        setAllignCenterCellStyle(wb, cell);
        setColor(wb, cell);
        if (currentRow == 0) {
            CellStyle style = cell.getCellStyle();
            style.setBorderTop(XSSFCellStyle.BORDER_THIN);

            cell.setCellStyle(style);
        }
    }

    private void printPair(int pairIndex, int dayIndex, int weekIndex, int currentRow, Workbook wb, Sheet sheet, Semester semester) {
        Pair pair = semester.getWeek(weekIndex).getDay(dayIndex).getPair(pairIndex);

        Row row = sheet.createRow(currentRow);

        createNumberCell(pair, wb, row);
        createTimeCell(pairIndex, wb, dayIndex, weekIndex, semester, row);
        createSubjectCell(pairIndex, wb, dayIndex, weekIndex, semester, row);
        createTypeCell(pairIndex, wb, dayIndex, weekIndex, semester, row);
        createTeacherCell(pairIndex, wb, dayIndex, weekIndex, semester, row);
        createLectureHallNumberCell(pairIndex, wb, dayIndex, weekIndex, semester, row);

        setColumnWidths(sheet);
    }

    private void setColumnWidths(Sheet sheet) {
        sheet.setColumnWidth(0, ONE_SYMBOL_COLUMN_WIDTH);
        sheet.setColumnWidth(3, ONE_SYMBOL_COLUMN_WIDTH);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(4);
    }

    private void createLectureHallNumberCell(int pairIndex, Workbook wb, int dayIndex, int weekIndex, Semester semester, Row row) {
        Cell cell = row.createCell(5);
        cell.setCellValue(semester.getWeek(weekIndex).getDay(dayIndex).getPair(pairIndex).getLectureHallNumber());

        setAllignCenterCellStyle(wb, cell);

        setAllBorders(cell, wb);
    }

    private void createTeacherCell(int pairIndex, Workbook wb, int dayIndex, int weekIndex, Semester semester, Row row) {
        Cell cell = row.createCell(4);
        cell.setCellValue(semester.getWeek(weekIndex).getDay(dayIndex).getPair(pairIndex).getTeacher());

        setAllBorders(cell, wb);
    }

    private void createTypeCell(int pairIndex, Workbook wb, int dayIndex, int weekIndex, Semester semester, Row row) {
        Cell cell = row.createCell(3);
        cell.setCellValue(Character.toString(semester.getWeek(weekIndex).getDay(dayIndex).getPair(pairIndex).getType()));

        setAllignCenterCellStyle(wb, cell);

        setAllBorders(cell, wb);
    }

    private void createSubjectCell(int pairIndex, Workbook wb, int dayIndex, int weekIndex, Semester semester, Row row) {
        Cell cell = row.createCell(2);

        cell.setCellValue(semester.getWeek(weekIndex).getDay(dayIndex).getPair(pairIndex).getSubject());

        setAllBorders(cell, wb);
    }

    private void createTimeCell(int pairIndex, Workbook wb, int dayIndex, int weekIndex, Semester semester, Row row) {
        Cell cell = row.createCell(1);
        // 09:00-10:20
        cell.setCellValue(String.format("%02d:%02d-%02d:%02d", semester.getWeek(weekIndex).getDay(dayIndex).getPair(pairIndex).getStartTime().getHour(),
                semester.getWeek(weekIndex).getDay(dayIndex).getPair(pairIndex).getStartTime().getMinute(),
                semester.getWeek(weekIndex).getDay(dayIndex).getPair(pairIndex).getStartTime().plusMinutes(80).getHour(),
                semester.getWeek(weekIndex).getDay(dayIndex).getPair(pairIndex).getStartTime().plusMinutes(80).getMinute()));

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

    private String getdayTitle(int dayIndex, int weekIndex, Semester semester) {
        return String.format("%s %02d.%02d", semester.getWeek(weekIndex).getDay(dayIndex).getStringName(),
                semester.getWeek(weekIndex).getDay(dayIndex).getDate().getDayOfMonth(),
                semester.getWeek(weekIndex).getDay(dayIndex).getDate().getMonthValue());
    }

}
