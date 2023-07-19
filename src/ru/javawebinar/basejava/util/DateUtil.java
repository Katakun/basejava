package ru.javawebinar.basejava.util;

import java.time.LocalDate;
import java.time.Month;

/**
 * gkislin
 * 20.07.2016
 */
public class DateUtil {

    public static final LocalDate NOW = LocalDate.of(3000, 1, 1);

    public static LocalDate of(int year, Month month) {
        if (year == 0 && month == null) {
            return LocalDate.of(0, Month.JANUARY, 1);
        } else if (year == 0) {
            return LocalDate.of(0, month, 1);
        } else  if (month == null){
            return LocalDate.of(year, Month.JANUARY, 1);
        }
        return LocalDate.of(year, month, 1);
    }

    // "2022-01" -> int year
    public static int getYear(String date) {
        if (date.length() == 0) {
            return 0;
        }
        String[] dates = date.split("-");
        String yearString = dates[0];
        int year = Integer.parseInt(yearString);
        return year;
    }

    // "2022-01" -> Month month
    public static Month getMonth(String date) {
        if (date.length() == 0) {
            return null;
        }
        String[] dates = date.split("-");
        String monthString = dates[1];
        int month = Integer.parseInt(monthString);
        return Month.of(month);
    }
}