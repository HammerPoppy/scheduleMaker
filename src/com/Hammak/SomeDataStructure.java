package com.Hammak;

import java.time.LocalDate;

public class SomeDataStructure {
    private int lectureHallNumber;
    private LocalDate startDay;
    private LocalDate endDay;

    public SomeDataStructure() {

    }

    public SomeDataStructure(int lectureHallNumber, LocalDate startDay, LocalDate endDay) {
        this.lectureHallNumber = lectureHallNumber;
        this.startDay = startDay;
        this.endDay = endDay;
    }

    public int getLectureHallNumber() {
        return lectureHallNumber;
    }

    public void setLectureHallNumber(int lectureHallNumber) {
        this.lectureHallNumber = lectureHallNumber;
    }

    public LocalDate getStartDay() {
        return startDay;
    }

    public void setStartDay(LocalDate startDay) {
        this.startDay = startDay;
    }

    public LocalDate getEndDay() {
        return endDay;
    }

    public void setEndDay(LocalDate endDay) {
        this.endDay = endDay;
    }
}
