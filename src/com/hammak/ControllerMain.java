package com.hammak;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ControllerMain {

    // Destination folder area
    public Label lDestinationFolder;
    public Button bSetCustomDestinationFolder;
    public Button bResetDestinationFolder;
    private File currentDestinationFolder;
    private File userDestinationFolder;
    // filePicker area
    public GridPane gpList;
    public ScrollPane spList;
    private HashSet<File> fileList;
    private boolean listIsEmpty = true;

    public ColorPicker colorPicker;

    public PreView preView;
    public ProgressBar progressBar;

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

        Semester semester = FileParser.readSemester(fileList.iterator().next());
        preView.fill(semester);

    }

    private void addFileRecord(File file, int index) {

        String filePath = String.valueOf(file);
        int lastSlashPosition = filePath.lastIndexOf('\\');
        String fileName = filePath.substring(lastSlashPosition + 1);

        Label fileNameLabel = new Label(fileName);
        Button deleteFileButton = new Button("x");

        deleteFileButton.setOnAction(event -> {
            fileList.remove(file);
            if (fileList.isEmpty()) {
                listIsEmpty = true;
            }
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

        setDestinationFolder();

    }

    private void setDestinationFolder() {
        if (!listIsEmpty) {
            if (userDestinationFolder == null) {
                String filePath = fileList.iterator().next().getAbsolutePath();
                int lastSlashIndex = filePath.lastIndexOf('\\');
                currentDestinationFolder = new File(filePath.substring(0, lastSlashIndex));
            }
            lDestinationFolder.setText(currentDestinationFolder.getAbsolutePath());
        } else {
            if (userDestinationFolder != null) {
                lDestinationFolder.setText(userDestinationFolder.getAbsolutePath() + "\nNo files... Add some files to proceed.");
            } else {
                lDestinationFolder.setText("No files... Add some files to proceed.");
                currentDestinationFolder = null;
            }
        }
    }

    private void deleteAllFileRecords() {
        gpList.getChildren().clear();
        setDestinationFolder();
    }

    public void deleteAllFiles() {
        deleteAllFileRecords();
        fileList.clear();
        listIsEmpty = true;
        setDestinationFolder();
    }

    @FXML
    public void initialize() {
        fileList = new HashSet<>();
    }

    public void setCustomDestinationFolder(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose save directory");
        userDestinationFolder = directoryChooser.showDialog(((Node) actionEvent.getTarget()).getScene().getWindow());
        currentDestinationFolder = userDestinationFolder;
        bResetDestinationFolder.setDisable(false);
        setDestinationFolder();
    }

    public void resetDestinationFolder(ActionEvent actionEvent) {
        userDestinationFolder = null;
        bResetDestinationFolder.setDisable(true);
        setDestinationFolder();
    }
    public void process(){
        new Thread(new Runnable() {


            @Override
            public void run() {

                DoubleProperty progressProperty = progressBar.progressProperty();

                progressProperty.addListener((observable, oldValue, newValue) -> {
                    System.out.println(newValue);
                    progressBar.setProgress(newValue.doubleValue());
                });
                progressProperty.setValue(0.5);
                List<Semester> semesters = FileParser.readAllSemesters(fileList,progressProperty);
                System.out.println("Loaded");
                FileParser.writeAllSemestersToFiles(semesters,currentDestinationFolder,progressProperty);
                System.out.println("wrote");
            }
        }).start();

    }
}
