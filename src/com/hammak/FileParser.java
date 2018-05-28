package com.hammak;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.ProgressBar;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FileParser {

    private static final int DETAILS_START_LINE = 26;
    private static final int DETAILS_END_SHIFT = 4;
    private static final String CHARSET = "windows-1251";
    private static final int TABLE_START_LINE = 8;
    private static final int TABLE_END_LINE = 24;

    public static List<Semester> readAllSemesters(HashSet<File> files, DoubleProperty fullProgress) {
        ArrayList<Semester> semesters = new ArrayList<>(files.size());
        fullProgress.set(0);
        for (File file : files) {
            semesters.add(readSemester(file));
            fullProgress.set(fullProgress.getValue() + 1.0 / files.size());
        }
        return semesters;
    }

    public static void writeAllSemestersToFiles(List<Semester> semesters, File destinationFolder, DoubleProperty fullProgress) {
        if (semesters.size() == 1) {
            Platform.runLater(() -> {
                fullProgress.set(ProgressBar.INDETERMINATE_PROGRESS);
            });
            for (Semester semester : semesters) {
                writeToFile(semester, destinationFolder);
            }
        } else {
            fullProgress.set(0);
            for (int i = 0; i < semesters.size(); i++) {
                writeToFile(semesters.get(i), destinationFolder);
                fullProgress.set(fullProgress.getValue() + 1.0 / semesters.size());
            }
        }

    }

    static Semester readSemester(File file) {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(file.toPath(),
                    Charset.forName(CHARSET));
        } catch (IOException e) {
            return null;
        }
        int year = getYear(lines);
        Semester semester = TableParser.getUnfilledSemester(lines.subList(TABLE_START_LINE, TABLE_END_LINE), year);

        semester = DetailsParser.fillSemester(semester, lines.subList(DETAILS_START_LINE, lines.size() - DETAILS_END_SHIFT), year);

        semester.setName(file.getName());

        return semester;
    }

    static int writeToFile(Semester semester, File destinationFolder) {
        ExcelPrinter excelPrinter = new ExcelPrinter();
        File filename = getFilename(semester.getName(), destinationFolder);
        try {
            excelPrinter.printSemester(semester, filename);
        } catch (IOException e) {
            // returns 404 for FIleNotFoundException
            return 404;
        }
        return 0;
    }

    private static File getFilename(String fileName, File destinationFolder) {
        String result = destinationFolder.toString() + "\\" + fileName;
        result = result.substring(0, result.lastIndexOf('.')) + ".xlsx";
        return new File(result);
    }

    static int getYear(List<String> lines) {
        String line = lines.get(0);
        int yearIndex = line.lastIndexOf('.') + 1;
        return Integer.parseInt(line.substring(yearIndex));
    }
}
