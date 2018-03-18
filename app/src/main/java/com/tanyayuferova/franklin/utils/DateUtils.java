package com.tanyayuferova.franklin.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tanya Yuferova on 10/5/2017.
 */

public class DateUtils {

    /**
     * Creates new date with added amount of days
     * @param date current date
     * @param days how many days to add to current date
     * @return date + days
     */
    public static Date addDaysToDate(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    /**
     * Gets first day of current week
     * @return date
     */
    public static Date getFirstDayOfWeek() {
        Calendar today = Calendar.getInstance();
        while (today.get(Calendar.DAY_OF_WEEK) != today.getFirstDayOfWeek()) {
            today.add(Calendar.DAY_OF_MONTH, -1);
        }
        return today.getTime();
    }

    /**
     * Checks if date is today
     * @param date date
     * @return true if date is today
     */
    public static boolean isToday(Date date) {
        Calendar today = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && today.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                && today.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Checks if first date is the same day as the second date
     * @param first
     * @param second
     * @return true if both date are the same day
     */
    public static boolean isTheSameDay(Date first, Date second) {
        Calendar _first = Calendar.getInstance();
        Calendar _second = Calendar.getInstance();

        _first.setTime(first);
        _second.setTime(second);

        return _first.get(Calendar.YEAR) == _second.get(Calendar.YEAR)
                && _first.get(Calendar.MONTH) == _second.get(Calendar.MONTH)
                && _first.get(Calendar.DAY_OF_MONTH) == _second.get(Calendar.DAY_OF_MONTH);
    }
}
