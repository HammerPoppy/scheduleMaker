package com.Hammak;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class DetailsParser {

    private static final int ASSEMBLY_HALL_NUMBER = 403;
    // МЕГА КОСТЫЛЬ (МНЕ ЛЕНЬ ОПРЕДЕЛЯТЬ ГОД ИЗ ФАЙЛА)
    // влияет на LocalDate каждого дня (в getCurrentDayDate()),
    // то есть день недели тоже, так что для каждого года надо перекомпилировать проги. сук, надо переделать
    private static final int PLACE_SCHEDULE_OFFSET = 3;
    private static final LocalTime PAIR_6_START_TIME;
    private static final LocalTime PAIR_7_START_TIME;
    static{
        PAIR_6_START_TIME = LocalTime.of(16, 40);
        PAIR_7_START_TIME = LocalTime.of(18, 10);
    }

    private static int getBlocksAmount(String line) {

        // |ауд.213 (01.03)|ауд.205 (08.03-15.03)|ауд.212 (22.03-12.04)|
        int blocksAmount = 0;

        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '|' && i != line.length() - 1) {
                blocksAmount++;
            }
        }

        return blocksAmount;
    }

    public static Semester fillSemester(Semester semester,List<String> lines,int year) {

        int i = 0;
        String line = lines.get(i);

        while (!(line.charAt(0) == ' ')) {
            if (Character.isLetter(line.charAt(0))) {
                DayOfWeek dayOfWeek = parseDayOfWeek(line);

                i++;
                line = lines.get(i);

                while (line.charAt(0) != '-') {
                    while (Character.isDigit(line.charAt(0))) {

                        // 0123456789
                        // 1 пара - 9:00
                        // 2 пара - 12:10
                        int pairNumber = Integer.parseInt(line.substring(0, 1));
                        LocalTime startTime = parseStartTime(line, pairNumber);

                        i++;
                        line = lines.get(i);

                        while (line.charAt(0) == '*') {
                            // 01234567890123456789012345678901234567890123456789
                            // * Інформаційні системи в обліку та аудиті (л) [ас. Духновська]
                            // * Корпоративні інформаційні системи (L) [доц. Сокульський]
                            //    |ауд.209 (05.03-19.03)
                            String subject = parseSubject(line);
                            String teacher = parseTeacher(line);

                            i++;
                            line = lines.get(i);

                            while (line.charAt(PLACE_SCHEDULE_OFFSET) == '|') {

                                // 012345678901234567890123456789
                                //    |ауд.213 (01.03)|ауд.205 (08.03-15.03)|ауд.212 (22.03-12.04)|
                                //    |ауд.217 (19.04-26.04)
                                ArrayList<SomeDataStructure> someDataStructures = parseHardPart(line,year);
                                for (SomeDataStructure someDataStructure : someDataStructures) {
                                    fillPairs(semester,pairNumber, startTime, subject, teacher,
                                            someDataStructure.getLectureHallNumber(),
                                            dayOfWeek,
                                            someDataStructure.getStartDay(),
                                            someDataStructure.getEndDay());
                                }

                                i++;
                                line = lines.get(i);
                            }
                        }

                    }
                }
            }
            i++;
            line = lines.get(i);
        }
        return semester;

    }
    
    private static ArrayList<SomeDataStructure> parseHardPart(String line,int year) {

        line = line.substring(PLACE_SCHEDULE_OFFSET);
        // 012345678901234567890123456789
        // |ауд.213 (01.03)|ауд.205 (08.03-15.03)|ауд.212 (22.03-12.04)|
        // |ауд.217 (19.04-26.04)
        int blocksAmount = getBlocksAmount(line);
        ArrayList<SomeDataStructure> someDataStructures = new ArrayList<>(blocksAmount);
        for (int i = 0; i < blocksAmount; i++) {
            someDataStructures.add(new SomeDataStructure());

            // 012345678901234567890123456789
            // |ауд.213 (01.03)
            if (line.substring(5, 7).equals("АЗ")) {
                someDataStructures.get(i).setLectureHallNumber(DetailsParser.ASSEMBLY_HALL_NUMBER);
                line = line.substring(9);
            } else {
                someDataStructures.get(i).setLectureHallNumber(Integer.parseInt(line.substring(5, 8)));
                line = line.substring(10);
            }

            // 012345678901234567890123456789
            // 01.03)
            // 19.04-26.04)
            int startDayDay = Integer.parseInt(line.substring(0, 2));
            int startDayMonth = Integer.parseInt(line.substring(3, 5));
            LocalDate startDay = LocalDate.of(year, startDayMonth, startDayDay);
            someDataStructures.get(i).setStartDay(startDay);


            if (line.charAt(5) == '-') {
                line = line.substring(6);
                // 0123456789
                // 26.04)

                int endDayDay = Integer.parseInt(line.substring(0, 2));
                int endDayMonth = Integer.parseInt(line.substring(3, 5));
                someDataStructures.get(i).setEndDay(LocalDate.of(year, endDayMonth, endDayDay));
                line = line.substring(6);
            } else {
                someDataStructures.get(i).setEndDay(startDay);
                line = line.substring(6);
            }

        }

        return someDataStructures;
    }

    private static String parseTeacher(String line) {

        // 01234567890123456789
        // * Корпоративні інформаційні системи (L) [доц. Сокульський][ ще хтось ]
        StringBuilder teacher = new StringBuilder("");
        int openingSquareBracketIndex = 0;
        int closingSquareBracketIndex = 0;
        while(line.indexOf('[',openingSquareBracketIndex+1) != -1){
            openingSquareBracketIndex = line.indexOf('[',openingSquareBracketIndex+1);
            closingSquareBracketIndex = line.indexOf(']',closingSquareBracketIndex+1);
            teacher.append(line.substring(openingSquareBracketIndex + 1, closingSquareBracketIndex));
            if(line.indexOf('[',openingSquareBracketIndex+1) != -1){
                teacher.append(" / ");
            }
        }
        return teacher.toString();
    }

    private static String parseSubject(String line) {

        // 0123456789012345678901234567890123456789
        // * Корпоративні інформаційні системи (L) [доц. Сокульський]
        int subjectEndIndex = line.lastIndexOf('(')-1;

        return line.substring(2, subjectEndIndex);
    }

    private static LocalTime parseStartTime(String line, int pairNumber) {

        // 0123456789
        // 1 пара - 9:00
        // 2 пара - 12:10
        if (pairNumber == 6) {
            return PAIR_6_START_TIME;
        }
        if (pairNumber == 7) {
            return PAIR_7_START_TIME;
        } else {
            int doubleDotIndex = line.indexOf(':');
            int startTimeHours = Integer.parseInt(line.substring(doubleDotIndex - 2, doubleDotIndex).replace(" ",""));
            int startTimeMinutes = Integer.parseInt(line.substring(doubleDotIndex + 1));

            return LocalTime.of(startTimeHours, startTimeMinutes);
        }
    }


    private static DayOfWeek parseDayOfWeek(String line) {

        // 0123456789
        // Понеділок
        // Вівторок

        HashMap<String, DayOfWeek> weekDays = new HashMap<>();
        weekDays.put("Понеділок", DayOfWeek.MONDAY);
        weekDays.put("Вівторок", DayOfWeek.TUESDAY);
        weekDays.put("Середа", DayOfWeek.WEDNESDAY);
        weekDays.put("Четвер", DayOfWeek.THURSDAY);
        weekDays.put("П\"ятниця", DayOfWeek.FRIDAY);
        weekDays.put("Субота", DayOfWeek.SATURDAY);

        return weekDays.get(line);
    }

    private static void fillPairs(Semester semester, int pairNumber, LocalTime startTime, String subject, String teacher, int lectureHallNumber,
                           DayOfWeek dayOfWeek, LocalDate startDay, LocalDate endDay) {

        for (int i = 0; i < semester.getWeeksAmount(); i++) {
            for (int j = 0; j < semester.getWeek(i).daysAmount(); j++) {
                if (semester.getWeek(i).getDay(j).getDate().isAfter(startDay.minusDays(1))) {

                    if (semester.getWeek(i).getDay(j).getDate().getDayOfWeek() == dayOfWeek) {
                        semester.getWeek(i).getDay(j).fillPair(pairNumber, startTime, lectureHallNumber, subject, teacher);
                    }

                    if (semester.getWeek(i).getDay(j).getDate().isAfter(endDay.plusDays(1))) {
                        return;
                    }
                }
            }
        }
    }
}
