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

    public Pair(int number, LocalTime startTime) {
        this.empty = true;
        this.number = number;
        this.startTime = startTime;
    }

    public Pair(int number, LocalTime startTime, String subject, char type, String teacher, int lectureHallNumber) {
        this.empty = false;
        this.number = number;
        this.startTime = startTime;
        this.subject = subject;
        this.type = type;
        this.teacher = teacher;
        this.lectureHallNumber = lectureHallNumber;
    }

    public boolean isEmpty() {
        return empty;
    }

    public int getNumber() {
        return number;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public String getSubject() {
        return subject;
    }

    public char getType() {
        return type;
    }

    public String getTeacher() {
        return teacher;
    }

    public int getLectureHallNumber() {
        return lectureHallNumber;
    }
}
