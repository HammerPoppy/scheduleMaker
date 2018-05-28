package com.hammak;

import javafx.beans.property.DoubleProperty;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class fileParser {

    private static final int DETAILS_START_LINE = 26;
    private static final int DETAILS_END_SHIFT = 4;
    private static final String CHARSET = "windows-1251";
    private static final int TABLE_START_LINE = 8;
    private static final int TABLE_END_LINE = 24;

    static List<Semester> readAllSemesters(List<String> files, DoubleProperty fullProgress, DoubleProperty fileProgress) {
        ArrayList<Semester> semesters = new ArrayList<>(files.size());
        fullProgress.set(0);
        for (String path : files) {
            semesters.add(readSemester(path,fileProgress));
            fullProgress.add(1.0 / files.size());
        }
        return semesters;
    }
    static void writeAllSemestersToFiles(List<Semester> semesters, List<String> files, DoubleProperty fullProgress, DoubleProperty fileProgress){
        fullProgress.set(0);
        for(int i = 0; i < semesters.size();i++){
            writeToFile(semesters.get(i), files.get(i), fileProgress);
            fullProgress.add(1.0 / semesters.size());
        }

    }

    static Semester readSemester(String fileName, DoubleProperty progress) {
        progress.set(0);
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(fileName),
                    Charset.forName(CHARSET));
        } catch (IOException e) {
            return null;
        }
        int year = getYear(lines);
        progress.set(0.05);
        Semester semester = TableParser.getUnfilledSemester(lines.subList(TABLE_START_LINE, TABLE_END_LINE), year);
        progress.set(0.1);

        semester = DetailsParser.fillSemester(semester, lines.subList(DETAILS_START_LINE, lines.size() - DETAILS_END_SHIFT), year);
        progress.set(0.2);

        return semester;
    }

    static int writeToFile(Semester semester, String fileName, DoubleProperty progress) {
        ExcelPrinter excelPrinter = new ExcelPrinter();
        String filename = getFilename(fileName);
        try {
            excelPrinter.printSemester(semester, filename);
        } catch (IOException e) {
            // returns 404 for FIleNotFoundException
            return 404;
        }
        return 0;
    }
    private static String getFilename(String arg) {
        int dotIndex = arg.lastIndexOf('.');
        return arg.substring(0, dotIndex) + ".xlsx";
    }
    static int getYear(List<String> lines) {
        String line = lines.get(0);
        int yearIndex = line.lastIndexOf('.') + 1;
        return Integer.parseInt(line.substring(yearIndex));
    }
}
