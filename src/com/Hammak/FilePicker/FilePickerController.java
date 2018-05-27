package com.hammak.FilePicker;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;

public class FilePickerController implements Initializable {
    public GridPane gpList;
    public ScrollPane spList;

    private HashSet<File> fileList;
    private boolean listIsEmpty = true;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileList = new HashSet<>();
    }
}
