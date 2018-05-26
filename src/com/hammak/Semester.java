package com.hammak;

import java.util.ArrayList;

class Semester {
    private ArrayList<Week> weeks;

    Semester(ArrayList<Week> weeks) {
        this.weeks = weeks;
    }

    Week getWeek(int weekNumber) {
        return weeks.get(weekNumber);
    }

    int getWeeksAmount() {
        return weeks.size();
    }
}
