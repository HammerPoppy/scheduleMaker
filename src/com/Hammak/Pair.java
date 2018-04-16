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

    public Pair(int number, char type) {
        this.number = number;
        this.type = type;
    }

    public Pair(int number) {
        this.number = number;
        this.empty = true;
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

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public char getType() {
        return type;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getLectureHallNumber() {
        return lectureHallNumber;
    }

    public void setLectureHallNumber(int lectureHallNumber) {
        this.lectureHallNumber = lectureHallNumber;
    }
}
