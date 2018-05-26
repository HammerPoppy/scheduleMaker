package com.Hammak;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import sun.management.snmp.util.SnmpNamedListTableCache;

public class preViewFiller {

    public static void fillWeek(VBox weekBox, Week week){
        for (int i = 0; i < week.daysAmount(); i++) {
            if (!week.getDay(i).isEmpty()) {
                weekBox.getChildren().add(fillDay(week.getDay(i)));
            }
        }
    }

    static GridPane fillDay(Day day){
        GridPane dayContainer = new GridPane();
        ColumnConstraints numberColumn = new ColumnConstraints();
        ColumnConstraints pairColumn = new ColumnConstraints();
        ColumnConstraints subjectColumn = new ColumnConstraints();
        ColumnConstraints typeColumn = new ColumnConstraints();
        ColumnConstraints teacherColumn = new ColumnConstraints();
        ColumnConstraints pairHallNumberColumn = new ColumnConstraints();

        numberColumn.setPercentWidth(4.5);
        pairColumn.setPercentWidth(12);
        subjectColumn.setPercentWidth(48);
        typeColumn.setPercentWidth(4.5);
        teacherColumn.setPercentWidth(24);
        pairHallNumberColumn.setPercentWidth(7);

        dayContainer.getColumnConstraints().setAll(numberColumn,pairColumn,subjectColumn,typeColumn,teacherColumn,pairHallNumberColumn);

        Label titleLabel = new Label(ExcelPrinter.getdayTitle(day));
        configureLabel(titleLabel);
        StackPane title = new StackPane(titleLabel);
        title.setPrefHeight(20);
        configureContainer(title);
        title.setStyle("-fx-background-color: green");
        dayContainer.add(title,0,0,6,1);


        for (int i = 0; i < day.pairsAmount(); i++) {
            if (!day.getPair(i).isEmpty()) {
                fillPair(day.getPair(i), dayContainer,i+1);
            }
        }
        return dayContainer;
    }
    static void fillPair(Pair pair, GridPane pairContainer,int rowIndex){
        Label pairNumber = new Label(pair.getNumber() + "");
        StackPane numberBox = new StackPane(pairNumber);

        Label pairTime = new Label(String.format("%02d:%02d-%02d:%02d", pair.getStartTime().getHour(),
                pair.getStartTime().getMinute(),
                pair.getStartTime().plusMinutes(80).getHour(),
                pair.getStartTime().plusMinutes(80).getMinute()));
        StackPane pairTimeBox = new StackPane(pairTime);

        Label pairSubject = new Label(pair.getSubject());
        StackPane subjectBox = new StackPane(pairSubject);

        Label pairType = new Label(pair.getType() + "");
        StackPane typeBox = new StackPane(pairType);

        Label pairTeacher = new Label(pair.getTeacher());
        StackPane teacherBox = new StackPane(pairTeacher);

        Label pairHallNumber = new Label(pair.getLectureHallNumber() + "");
        StackPane hallNumberBox = new StackPane(pairHallNumber);

        configureLabel(pairNumber);
        configureLabel(pairTime);
        configureLabel(pairSubject);
        configureLabel(pairType);
        configureLabel(pairTeacher);
        configureLabel(pairHallNumber);

        configureContainer(numberBox);
        configureContainer(pairTimeBox);
        configureContainer(subjectBox);
        configureContainer(typeBox);
        configureContainer(teacherBox);
        configureContainer(hallNumberBox);

        pairContainer.add(numberBox,0,rowIndex);
        pairContainer.add(pairTimeBox,1,rowIndex);
        pairContainer.add(subjectBox,2,rowIndex);
        pairContainer.add(typeBox,3,rowIndex);
        pairContainer.add(teacherBox,4,rowIndex);
        pairContainer.add(hallNumberBox,5,rowIndex);
    }
    private static void configureLabel(Label label){
        label.setStyle("-fx-padding: 5px");
    }
    private static void configureContainer(Parent container){
        container.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-width: 2px");
        //container.prefHeight(LINE_HEIGHT);
    }
}
