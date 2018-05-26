package com.hammak;

import javafx.beans.property.DoubleProperty;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ControllerMain {

    private static final int DETAILS_START_LINE = 26;
    private static final int DETAILS_END_SHIFT = 4;
    private static final String CHARSET = "windows-1251";
    private static final int TABLE_START_LINE = 8;
    private static final int TABLE_END_LINE = 24;

    static int makeAllFiles(List<String> files, DoubleProperty fullProgress, DoubleProperty fileProgress){
        fullProgress.set(0);
        for(String path:files){
            makeFile(path,fileProgress);
            fullProgress.add(1.0/files.size());
        }
        return 0;
    }
    // returns 404 for FIleNotFoundException
    static int makeFile(String fileName, DoubleProperty progress) {
        progress.set(0);
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(fileName),
                    Charset.forName(CHARSET));
        } catch (IOException e) {
            return 404;
        }
        int year = getYear(lines);
        progress.set(0.05);
        Semester semester = TableParser.getUnfilledSemester(lines.subList(TABLE_START_LINE, TABLE_END_LINE),year);
        progress.set(0.1);

        semester = DetailsParser.fillSemester(semester, lines.subList(DETAILS_START_LINE, lines.size() - DETAILS_END_SHIFT),year);
        progress.set(0.2);

        ExcelPrinter excelPrinter = new ExcelPrinter();
        String filename = getFilename(fileName);
        try {
            excelPrinter.printSemester(semester, filename);
        } catch (IOException e) {
            return 404;
        }

        return 0;
    }

    private static String getFilename(String arg) {
        int dotIndex = arg.lastIndexOf('.');
        return arg.substring(0, dotIndex) + ".xlsx";
    }
    static int getYear(List<String> lines){
        String line = lines.get(0);
        int yearIndex = line.lastIndexOf('.') + 1;
        return Integer.parseInt(line.substring(yearIndex));
    }

}
