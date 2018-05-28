package com.hammak;

import java.util.ArrayList;

class Semester {
    private ArrayList<Week> weeks;
    private String name;
    Semester(ArrayList<Week> weeks) {
        this.weeks = weeks;
    }

    Week getWeek(int weekNumber) {
        return weeks.get(weekNumber);
    }

    int getWeeksAmount() {
        return weeks.size();
    }

    String getName(){
        return  name;
    }

    void setName(String name){
        this.name = name;
    }
}
