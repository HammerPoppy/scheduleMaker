package com.Hammak;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import sun.management.snmp.util.SnmpNamedListTableCache;

public class preViewFiller {
    public static void fillWeek(VBox weekBox, Week week){
        for (int i = 0; i < week.daysAmount(); i++) {
            if (!week.getDay(i).isEmpty()) {
                VBox dayBox = new VBox();
                fillDay(dayBox, week.getDay(i));
                weekBox.getChildren().add(dayBox);
            }
        }
    }

    static void fillDay(VBox dayContainer,Day day){
        Label titleLabel = new Label(ExcelPrinter.getdayTitle(day));
        Pane title = new Pane(titleLabel);
        title.setPrefHeight(20);
        title.setStyle("-fx-background-color: green");
        dayContainer.getChildren().add(title);


        for (int i = 0; i < day.pairsAmount(); i++) {
            if (!day.getPair(i).isEmpty()) {
                HBox pairBox = new HBox();
                fillPair(day.getPair(i), pairBox);//filling HBox
                pairBox.setStyle("-fx-background-color: red");

                dayContainer.getChildren().add(pairBox);
            }
        }

    }
    static void fillPair(Pair pair, HBox pairContainer){
        Label pairNumber = new Label(pair.getNumber() + "");
        Pane numberBox = new Pane(pairNumber);

        Label pairTime = new Label(String.format("%02d:%02d-%02d:%02d", pair.getStartTime().getHour(),
                pair.getStartTime().getMinute(),
                pair.getStartTime().plusMinutes(80).getHour(),
                pair.getStartTime().plusMinutes(80).getMinute()));
        Pane pairTimeBox = new Pane(pairTime);

        Label pairSubject = new Label(pair.getSubject());
        Pane subjectBox = new Pane(pairSubject);
        subjectBox.setPrefWidth(400);

        Label pairType = new Label(pair.getType() + "");
        Pane typeBox = new Pane(pairType);

        Label pairTeacher = new Label(pair.getTeacher());
        Pane teacherBox = new Pane(pairTeacher);

        Label pairHallNumber = new Label(pair.getLectureHallNumber() + "");
        Pane hallNumberBox = new Pane(pairHallNumber);

        pairContainer.getChildren().addAll(numberBox,pairTimeBox,subjectBox,typeBox,teacherBox,hallNumberBox);
    }
}
