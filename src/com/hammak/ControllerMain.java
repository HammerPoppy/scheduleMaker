package com.hammak;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ControllerMain {

    public static final String DEFAULT_COLOR = "#ADFF2F";
    // Destination folder area
    public Label lDestinationFolder;
    public Button bSetCustomDestinationFolder;
    public Button bResetDestinationFolder;
    // filePicker area
    public GridPane gpList;
    public ScrollPane spList;
    public Button bDeleteAllFiles;
    public ColorPicker colorPicker;
    public PreView preView;
    public ProgressBar progressBar;
    public Button bStart;
    private File currentDestinationFolder;
    private File userDestinationFolder;
    private HashSet<File> fileList;
    private boolean listIsEmpty = true;

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
            bDeleteAllFiles.setDisable(false);
            bStart.setDisable(false);

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
        fileNameLabel.setCursor(Cursor.HAND);

        fileNameLabel.setOnMouseClicked(event -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(300), fileNameLabel);
            st.setByX(0.2);
            st.setByY(0.2);
            st.setCycleCount(2);
            st.setAutoReverse(true);

            st.play();

            File clickedFile = findFile(fileNameLabel.getText());
            Semester semester = FileParser.readSemester(clickedFile);
            preView.fill(semester);

        });

        Button deleteFileButton = new Button("x");

        deleteFileButton.setOnAction(event -> {
            fileList.remove(file);
            if (fileList.isEmpty()) {
                listIsEmpty = true; 
                bDeleteAllFiles.setDisable(true);
                bStart.setDisable(true);
            }
            repaintGUIList();
        });

        gpList.add(fileNameLabel, 0, index);
        gpList.add(deleteFileButton, 1, index);
    }

    private File findFile(String fileName) {
        File result = null;
        Iterator<File> iterator = fileList.iterator();
        while (iterator.hasNext()) {
            File currentFile = iterator.next();
            if (currentFile.getName().equals(fileName)) {
                result = currentFile;
                break;
            }
        }
        return result;
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
        bDeleteAllFiles.setDisable(true);
        bStart.setDisable(true);
    }

    @FXML
    public void initialize() {
        fileList = new HashSet<>();
        colorPicker.setValue(Color.valueOf(DEFAULT_COLOR));
        preView.setColor(DEFAULT_COLOR);
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

    public void process() {
        bStart.setVisible(false);
        bStart.setManaged(false);
        GridPane.setColumnSpan(progressBar,2);
        new Thread(() -> {
            List<Semester> semesters = FileParser.readAllSemesters(fileList, progressBar.progressProperty());
            FileParser.writeAllSemestersToFiles(semesters, currentDestinationFolder, progressBar.progressProperty(), colorPicker.getValue().toString().substring(0, 8));
            Platform.runLater(() -> {
                GridPane.setColumnSpan(progressBar,1);
                bStart.setVisible(true);
                bStart.setManaged(true);
                progressBar.setProgress(0);
            });
        }).start();

    }
    public void changeColor(){
        Color color = colorPicker.getValue();
        preView.setColor(String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) ));
        preView.repaint();
    }
}
