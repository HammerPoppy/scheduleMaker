package com.Hammak;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static final int DETAILS_START_LINE = 26;
    public static final int DETAILS_END_SHIFT = 4;
    private static final String CHARSET = "windows-1251";
    private static final int TABLE_START_LINE = 8;
    private static final int TABLE_END_LINE = 24;

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(args[0]),
                Charset.forName(CHARSET));
        var tableHandler = new TableParser(lines.subList(TABLE_START_LINE, TABLE_END_LINE));
        Semester unfilledSemester = TableParser.getUnfilledSemester();

        var detailsHandler = new DetailsParser(lines.subList(DETAILS_START_LINE, lines.size() - DETAILS_END_SHIFT), unfilledSemester);
        Semester semester = DetailsParser.getSemester();


        System.out.println("Done!");
    }

}
