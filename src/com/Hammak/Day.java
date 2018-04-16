package com.Hammak;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Day {
    private boolean empty;
    private LocalDate date;
    private DayOfWeek name;
    private ArrayList<Pair> pairs;

    public Day(LocalDate date, ArrayList<Pair> pairs) {
        this.date = date;
        this.name = date.getDayOfWeek();
        this.pairs = pairs;
        this.empty = emptinessCheck();
    }

    public void fillPair(int pairNumber, LocalTime startTime, int lectureHallNumber, String subject, String teacher) {
        pairs.get(pairNumber - 1).setStartTime(startTime);
        pairs.get(pairNumber - 1).setLectureHallNumber(lectureHallNumber);
        pairs.get(pairNumber - 1).setSubject(subject);
        pairs.get(pairNumber - 1).setTeacher(teacher);

    }

    private boolean emptinessCheck() {
        for (Pair pair : pairs) {
            if (!pair.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        return empty;
    }

    public DayOfWeek getName() {
        return name;
    }

    public ArrayList<Pair> getPairs() {
        return pairs;
    }

    public LocalDate getDate() {
        return date;
    }

    public int pairsAmount() {
        return pairs.size();
    }

    public Pair getPair(int pairNumber) {
        return pairs.get(pairNumber);
    }
}
