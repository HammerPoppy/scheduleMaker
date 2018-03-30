package com.Hammak;

import java.time.LocalTime;

public class Pair {
    private boolean empty;

    public boolean isEmpty() {
        return empty;
    }

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
}
