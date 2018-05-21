package com.Hammak;

import java.time.LocalTime;

public class Pair {
    private boolean empty;
    private int number;
    private LocalTime startTime;
    private String subject;
    private char type;
    private String teacher;
    private int lectureHallNumber;

    Pair(int number, char type) {
        this.number = number;
        this.type = type;
    }

    Pair(int number) {
        this.number = number;
        this.empty = true;
    }

    boolean isEmpty() {
        return empty;
    }

    int getNumber() {
        return number;
    }

    LocalTime getStartTime() {
        return startTime;
    }

    void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    String getSubject() {
        return subject;
    }

    void setSubject(String subject) {
        this.subject = subject;
    }

    char getType() {
        return type;
    }

    String getTeacher() {
        return teacher;
    }

    void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    int getLectureHallNumber() {
        return lectureHallNumber;
    }

    void setLectureHallNumber(int lectureHallNumber) {
        this.lectureHallNumber = lectureHallNumber;
    }
}
