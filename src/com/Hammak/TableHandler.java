package com.Hammak;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TableHandler {
    private static final int DAYS_NUMBER = 6;
    private static final int PAIRS_NUMBER = 7;

    // МЕГА КОСТЫЛЬ (МНЕ ЛЕНЬ ОПРЕДЕЛЯТЬ ГОД ИЗ ФАЙЛА)
    // влияет на LocalDate каждого дня (в getCurrentDayDate()),
    // то есть день недели тоже, так что для каждого года надо перекомпилировать проги. сук, надо переделать
    private static final int CURRENT_YEAR = 2018;

    private static Semester semester;
    private List<String> lines;

    public TableHandler(List<String> lines) {
        this.lines = lines;
        semester = getSemesterTable();
    }

    public static Semester getSemester() {
        return semester;
    }

    public Semester getSemesterTable() {

        ArrayList<Week> weeks = getWeeksTable();

        var semester = new Semester(weeks);

        return semester;
    }

    private ArrayList<Week> getWeeksTable() {

        var weeks = new ArrayList<Week>();

        for (int i = 0; i < lines.size(); i++) {
            ArrayList<Day> days = getDaysTable(lines.get(i));

            var currentWeek = new Week(days);

            weeks.add(currentWeek);
        }
        return weeks;
    }

    private ArrayList<Day> getDaysTable(String line) {

        var days = new ArrayList<Day>();

        for (int j = 0; j < DAYS_NUMBER; j++) {
            ArrayList<Pair> pairs = getPairsTable(line, j);

            var currentDay = new Day(getCurrentDayDate(line, j), pairs);

            days.add(currentDay);
        }
        return days;
    }

    private ArrayList<Pair> getPairsTable(String line, int day) {
        ArrayList<Pair> pairs = new ArrayList<>();

        //0123456789
        //| 26.02 |.......|.......|.......|.......|.......|.......|
        //| 16.04 |ллL....|Lлл....|ПППП...|лLл....|лл.....|.......|

        line = line.substring(9);

        for (int i = 0; i < PAIRS_NUMBER; i++) {
            //0123456789
            //.......|.......|.......|.......|.......|.......|
            //ллL....|Lлл....|ПППП...|лLл....|лл.....|.......|

            Pair currentPair;

            if (line.charAt(i) == '.'){
                currentPair = new Pair(i);
            } else{
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

        LocalDate currentDayDate = LocalDate.of(CURRENT_YEAR, monthNum, dayNum).plusDays(shift);
        return currentDayDate;
    }
}
