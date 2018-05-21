package com.Hammak;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class TableParser {
    private static final int DAYS_NUMBER = 6;
    private static final int PAIRS_NUMBER = 7;

    // МЕГА КОСТЫЛЬ (МНЕ ЛЕНЬ ОПРЕДЕЛЯТЬ ГОД ИЗ ФАЙЛА)
    // влияет на LocalDate каждого дня (в getCurrentDayDate()),
    // то есть день недели тоже, так что для каждого года надо перекомпилировать проги. сук, надо переделать
    private static final int CURRENT_YEAR = 2018;

    private static Semester semester;
    private List<String> lines;

    TableParser(List<String> lines) {
        this.lines = lines;
        semester = getSemesterTable();
    }

    static Semester getUnfilledSemester() {
        return semester;
    }

    private Semester getSemesterTable() {

        ArrayList<Week> weeks = getWeeksTable();

        return new Semester(weeks);
    }

    private ArrayList<Week> getWeeksTable() {

        ArrayList<Week> weeks = new ArrayList<>();

        for (String line : lines) {
            ArrayList<Day> days = getDaysTable(line);

            Week currentWeek = new Week(days);

            weeks.add(currentWeek);
        }
        return weeks;
    }

    private ArrayList<Day> getDaysTable(String line) {

        ArrayList<Day> days = new ArrayList<>();

        for (int j = 0; j < DAYS_NUMBER; j++) {
            ArrayList<Pair> pairs = getPairsTable(line, j);

            Day currentDay = new Day(getCurrentDayDate(line, j), pairs);

            days.add(currentDay);
        }
        return days;
    }

    private ArrayList<Pair> getPairsTable(String line, int day) {
        ArrayList<Pair> pairs = new ArrayList<>();

        //0123456789
        //| 26.02 |.......|.......|.......|.......|.......|.......|
        //| 16.04 |ллL....|Lлл....|ПППП...|лLл....|лл.....|.......|

        line = line.substring(PAIRS_NUMBER + 2 + day * 8);

        for (int i = 0; i < PAIRS_NUMBER; i++) {
            //0123456789
            //.......|.......|.......|.......|.......|.......|
            //ллL....|Lлл....|ПППП...|лLл....|лл.....|.......|

            Pair currentPair;

            if (line.charAt(i) == '.') {
                currentPair = new Pair(i + 1);
            } else {
                currentPair = new Pair(i + 1, line.charAt(i));
            }

            pairs.add(currentPair);
        }

        return pairs;
    }

    private LocalDate getCurrentDayDate(String line, int shift) {

        int monthNum, dayNum;

        //0123456789
        //| 26.02 |.......|.......|.......|.......|.......|.......|
        //| 16.04 |ллL....|Lлл....|ПППП...|лLл....|лл.....|.......|

        monthNum = Integer.parseInt(line.substring(5, 7));
        dayNum = Integer.parseInt(line.substring(2, 4));

        return LocalDate.of(CURRENT_YEAR, monthNum, dayNum).plusDays(shift);
    }
}
