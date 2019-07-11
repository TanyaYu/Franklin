package com.tanyayuferova.franklin.utils

import java.util.Date
import java.util.Calendar
import java.util.Calendar.*
import java.util.concurrent.TimeUnit

/**
 * Author: Tanya Yuferova
 * Date: 7/7/2019
 */
fun Calendar.setYear(year: Int) = apply { set(YEAR, year) }

fun Calendar.setMonth(month: Int) = apply { set(MONTH, month) }
fun Calendar.setDate(date: Int) = apply { set(DATE, date) }
fun Calendar.setHourOfDay(hour: Int) = apply { set(HOUR_OF_DAY, hour) }
fun Calendar.setMinute(minute: Int) = apply { set(MINUTE, minute) }
fun Calendar.setSecond(second: Int) = apply { set(SECOND, second) }
fun Calendar.setMilliSecond(millisecond: Int) = apply { set(MILLISECOND, millisecond) }
fun Calendar.setDayOfWeek(dayOfWeek: Int) = apply { set(DAY_OF_WEEK, dayOfWeek) }
fun Calendar.setWeek(week: Int) = apply { set(WEEK_OF_YEAR, week) }

val Calendar.year get() = get(YEAR)
val Calendar.month get() = get(MONTH)
val Calendar.week get() = get(WEEK_OF_YEAR)
val Calendar.date get() = get(DATE)
val Calendar.hourOfDay get() = get(HOUR_OF_DAY)
val Calendar.minute get() = get(MINUTE)
val Calendar.second get() = get(SECOND)
val Calendar.millisecond get() = get(MILLISECOND)

fun Date.setUp(
    year: Int = year_, month: Int = month_, date: Int = date_,
    hour: Int = this.hour, minute: Int = this.minute,
    second: Int = this.second, millisecond: Int = this.millisecond
): Date {
    return Calendar.getInstance()
        .setYear(year)
        .setMonth(month)
        .setDate(date)
        .setHourOfDay(hour)
        .setMinute(minute)
        .setSecond(second)
        .setMilliSecond(millisecond)
        .time
}

val today = Date().removeTime()

val Date.yearOfWeek: Int
    get() {
        // This is necessary for the last week of the year, which is the first week of the next year as well
        return if (week == 1 && month_ == DECEMBER)
            year_ + 1
        else year_

    }
val Date.year_: Int get() = toCalendar().year
val Date.month_: Int get() = toCalendar().month
val Date.week: Int get() = toCalendar().week
val Date.date_: Int get() = toCalendar().date
val Date.hour: Int get() = toCalendar().hourOfDay
val Date.minute: Int get() = toCalendar().minute
val Date.second: Int get() = toCalendar().second
val Date.millisecond: Int get() = toCalendar().millisecond

var Date.totalMinutes: Int
    get() = minute + hour * 60
    set(minutes) {
        setUp(
            hour = minutes / 60,
            minute = minutes % 60,
            second = 0,
            millisecond = 0
        )
    }


fun Date.toCalendar() = Calendar.getInstance().apply { time = this@toCalendar }

fun Date.addDays(days: Int) = toCalendar().apply { add(DAY_OF_MONTH, days) }.time
fun Date.subtractDays(days: Int) = addDays(-days)

fun Date.removeTime(): Date {
    return toCalendar()
        .setHourOfDay(0)
        .setMinute(0)
        .setSecond(0)
        .setMilliSecond(0)
        .time
}

fun getFirstDayOfWeek(date: Date): Date {
    return date.toCalendar().setDayOfWeek(1).time
}

fun getFirstDayOfWeek(year: Int, week: Int) = Date().toCalendar()
    .setYear(year)
    .setWeek(week)
    .setDayOfWeek(1)
    .time

val MILLIS_IN_WEEK = TimeUnit.DAYS.toMillis(7)

/* Amount of full weeks passed since the date */
fun Date.weeksPassed(since: Date): Int {
    return ((this.time - since.time) / MILLIS_IN_WEEK).toInt()
}
