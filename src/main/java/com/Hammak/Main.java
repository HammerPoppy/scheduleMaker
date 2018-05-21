package com.Hammak;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    private static final int DETAILS_START_LINE = 26;
    private static final int DETAILS_END_SHIFT = 4;
    private static final String CHARSET = "windows-1251";
    private static final int TABLE_START_LINE = 8;
    private static final int TABLE_END_LINE = 24;

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(args[0]),
                Charset.forName(CHARSET));
        new TableParser(lines.subList(TABLE_START_LINE, TABLE_END_LINE));
        Semester unfilledSemester = TableParser.getUnfilledSemester();

        DetailsParser detailsParser = new DetailsParser(lines.subList(DETAILS_START_LINE, lines.size() - DETAILS_END_SHIFT), unfilledSemester);
        Semester semester = detailsParser.getSemester();

        ExcelPrinter excelPrinter = new ExcelPrinter();
        String filename = getFilename(args[0]);
        excelPrinter.printSemester(semester, filename);

        System.out.println("Done!");
    }

    private static String getFilename(String arg) {
        int dotIndex = arg.lastIndexOf('.');
        return arg.substring(0, dotIndex) + ".xlsx";
    }

}
