package com.Hammak;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

public class Day {
    private boolean empty;

    private LocalDate date;
    private DayOfWeek name;
    private ArrayList<Pair> pairs;

    public Day(LocalDate date, DayOfWeek name, ArrayList<Pair> pairs) {
        this.date = date;
        this.name = name;
        this.pairs = pairs;
        empty = emptinessCheck();
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
