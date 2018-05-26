package com.hammak;

import java.time.LocalDate;

class SomeDataStructure {
    private int lectureHallNumber;
    private LocalDate startDay;
    private LocalDate endDay;

    SomeDataStructure() {

    }

    int getLectureHallNumber() {
        return lectureHallNumber;
    }

    void setLectureHallNumber(int lectureHallNumber) {
        this.lectureHallNumber = lectureHallNumber;
    }

    LocalDate getStartDay() {
        return startDay;
    }

    void setStartDay(LocalDate startDay) {
        this.startDay = startDay;
    }

    LocalDate getEndDay() {
        return endDay;
    }

    void setEndDay(LocalDate endDay) {
        this.endDay = endDay;
    }
}
