package com.Hammak;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

public class Day {
    public boolean isEmpty() {
        return empty;
    }

    private boolean empty;

    private LocalDate date;

    public LocalDate getDate() {
        return date;
    }

    private DayOfWeek name;
    private ArrayList<Pair> pairs;

    public Day(LocalDate date, ArrayList<Pair> pairs) {
        this.date = date;
        this.name = date.getDayOfWeek();
        this.pairs = pairs;
        this.empty = emptinessCheck();
    }

    private boolean emptinessCheck() {
        for (Pair pair : pairs) {
            if (!pair.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
