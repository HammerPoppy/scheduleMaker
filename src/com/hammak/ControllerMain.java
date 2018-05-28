package com.hammak;

import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ControllerMain {

    // core area
    private static final int DETAILS_START_LINE = 26;
    private static final int DETAILS_END_SHIFT = 4;
    private static final String CHARSET = "windows-1251";
    private static final int TABLE_START_LINE = 8;
    private static final int TABLE_END_LINE = 24;

    // filePicker area
    @FXML
    private GridPane gpList;
    @FXML
    private ScrollPane spList;

    private HashSet<File> fileList;
    private boolean listIsEmpty = true;

    // core
    static int makeAllFiles(List<String> files, DoubleProperty fullProgress, DoubleProperty fileProgress) {
        fullProgress.set(0);
        for (String path : files) {
            makeFile(path, fileProgress);
            fullProgress.add(1.0 / files.size());
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
        Semester semester = TableParser.getUnfilledSemester(lines.subList(TABLE_START_LINE, TABLE_END_LINE), year);
        progress.set(0.1);

        semester = DetailsParser.fillSemester(semester, lines.subList(DETAILS_START_LINE, lines.size() - DETAILS_END_SHIFT), year);
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

    static int getYear(List<String> lines) {
        String line = lines.get(0);
        int yearIndex = line.lastIndexOf('.') + 1;
        return Integer.parseInt(line.substring(yearIndex));
    }

    //filePicker
    public void addFiles(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files(*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setTitle("Choose file(s)");

        if (!listIsEmpty) {
            String filePath = String.valueOf(fileList.iterator().next());
            int lastSlashPosition = filePath.lastIndexOf('\\');
            String folderPath = filePath.substring(0, lastSlashPosition);
            fileChooser.setInitialDirectory(new File(folderPath));
        }

        List<? extends File> receivedList = fileChooser.showOpenMultipleDialog(((Node) actionEvent.getTarget()).getScene().getWindow());
        if (receivedList != null) {
            fileList.addAll(receivedList);
            listIsEmpty = false;

            repaintGUIList();
        }

    }

    private void addFileRecord(File file, int index) {

        String filePath = String.valueOf(file);
        int lastSlashPosition = filePath.lastIndexOf('\\');
        String fileName = filePath.substring(lastSlashPosition + 1);

        Label fileNameLabel = new Label(fileName);
        Button deleteFileButton = new Button("x");

        deleteFileButton.setOnAction(event -> {
            fileList.remove(file);
            repaintGUIList();
        });

        gpList.add(fileNameLabel, 0, index);
        gpList.add(deleteFileButton, 1, index);
    }

    private void repaintGUIList() {

        deleteAllFileRecords();
        HashSet clone = (HashSet) fileList.clone();
        Iterator iterator = clone.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            addFileRecord((File) iterator.next(), i);
            i++;
            iterator.remove();
        }

    }

    private void deleteAllFileRecords() {
        gpList.getChildren().clear();
    }

    public void deleteAllFiles() {
        deleteAllFileRecords();
        fileList.clear();
        listIsEmpty = true;
    }

    @FXML
    public void initialize() {
        fileList = new HashSet<>();
    }

}
