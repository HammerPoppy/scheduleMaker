package com.hammak;

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

    // returns 404 for FIleNotFoundException
    int makeFile(String fileName) {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(fileName),
                    Charset.forName(CHARSET));
        } catch (IOException e) {
            return 404;
        }
        Semester semester = TableParser.getUnfilledSemester(lines.subList(TABLE_START_LINE, TABLE_END_LINE));

        semester = DetailsParser.fillSemester(semester, lines.subList(DETAILS_START_LINE, lines.size() - DETAILS_END_SHIFT));

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

}
