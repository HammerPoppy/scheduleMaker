package com.hammak.FilePicker;

import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

public class FilePickerController implements Initializable {
    public GridPane gpList;
    public ScrollPane spList;

    private HashSet<File> fileList;
    private boolean listIsEmpty = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileList = new HashSet<>();
    }
}
