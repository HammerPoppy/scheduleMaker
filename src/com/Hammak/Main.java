package com.Hammak;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    private static final String CHARSET = "windows-1251";

    private static final int TABLE_START_LINE = 8;
    private static final int TABLE_END_LINE = 24;

    public static void main(String[] args) throws IOException {
        var tableHandler = new TableHandler(Files.readAllLines(Paths.get(args[0]),
                Charset.forName(CHARSET)).subList(TABLE_START_LINE, TABLE_END_LINE));
    }
}
