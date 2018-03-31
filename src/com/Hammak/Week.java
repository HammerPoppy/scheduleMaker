package com.Hammak;

import java.time.LocalDate;
import java.util.ArrayList;

public class Week {
    private boolean empty;

    private LocalDate firstDayDate;
    private ArrayList<Day> days;

    public Week(ArrayList<Day> days) {
        this.days = days;
        this.firstDayDate = days.get(0).getDate();
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
}
