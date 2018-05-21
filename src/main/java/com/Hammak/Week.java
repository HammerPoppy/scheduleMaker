package com.Hammak;

import java.util.ArrayList;

class Week {
    private boolean empty;
    private ArrayList<Day> days;

    Week(ArrayList<Day> days) {
        this.days = days;
        this.empty = emptinessCheck();
    }

    private boolean emptinessCheck() {
        for (Day day : days) {
            if (!day.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    Day getDay(int dayNumber) {
        return days.get(dayNumber);
    }

    boolean isEmpty() {
        return empty;
    }

    int daysAmount() {
        return days.size();
    }
}
