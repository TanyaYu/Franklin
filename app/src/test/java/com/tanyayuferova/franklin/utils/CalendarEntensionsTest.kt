package com.tanyayuferova.franklin.utils

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.*

/**
 * Author: Tanya Yuferova
 * Date: 7/9/19
 */
class CalendarEntensionsTest {

    @Test
    fun removeTime() {
        val dateTime = Date().setUp(
            year = 2019, month = 3, date = 16,
            hour = 12, minute = 30
        )

        val dateNoTime = Date().setUp(
            year = 2019, month = 3, date = 16,
            hour = 0, minute = 0, second = 0, millisecond = 0
        )

        assertEquals(dateNoTime, dateTime.removeTime())
    }

    @Test
    fun totalMinutes() {
        val date750Min = Date().setUp(
            year = 2019, month = 3, date = 16,
            hour = 12, minute = 30
        )
        val date1259Min = Date().setUp(
            year = 2019, month = 5, date = 9,
            hour = 20, minute = 59
        )
        val date60Min = Date().setUp(
            year = 2019, month = 9, date = 19,
            hour = 1, minute = 0
        )
        val date0Min = Date().setUp(
            year = 2019, month = 1, date = 1,
            hour = 0, minute = 0
        )
        val date190Min = Date().setUp(
            year = 2019, month = 6, date = 10,
            hour = 3, minute = 10
        )

        assertEquals(750, date750Min.totalMinutes)
        assertEquals(1259, date1259Min.totalMinutes)
        assertEquals(60, date60Min.totalMinutes)
        assertEquals(0, date0Min.totalMinutes)
        assertEquals(190, date190Min.totalMinutes)
    }

    @Test
    fun getFirstDayOfWeek() {
        assertEquals(
            Date().setUp(2019, 6, 7, 0, 0, 0, 0),
            getFirstDayOfWeek(Date().setUp(2019, 6, 10)).removeTime()
        )
        assertEquals(
            Date().setUp(2019, 6, 28, 0, 0, 0, 0),
            getFirstDayOfWeek(Date().setUp(2019, 7, 1)).removeTime()
        )
        assertEquals(
            Date().setUp(2019, 11, 29, 0, 0, 0, 0),
            getFirstDayOfWeek(Date().setUp(2020, 0, 4)).removeTime()
        )
        assertEquals(
            Date().setUp(2020, 0, 5, 0, 0, 0, 0),
            getFirstDayOfWeek(Date().setUp(2020, 0, 5)).removeTime()
        )
        assertEquals(
            Date().setUp(2019, 11, 29, 0, 0, 0, 0),
            getFirstDayOfWeek(2020, 1).removeTime()
        )
        assertEquals(
            Date().setUp(2019, 11, 29, 0, 0, 0, 0),
            getFirstDayOfWeek(2019, 53).removeTime()
        )
    }

    @Test
    fun addDays() {
        val date = Date()
        assertEquals(
            date.setUp(2020, 0, 8),
            date.setUp(2020, 0, 1).addDays(7)
        )
        assertEquals(
            date.setUp(2019, 6, 10),
            date.setUp(2019, 5, 30).addDays(10)
        )
    }

    @Test
    fun subtractDays() {
        val date = Date()
        assertEquals(
            date.setUp(2020, 0, 1),
            date.setUp(2020, 0, 8).subtractDays(7)
        )
        assertEquals(
            date.setUp(2019, 5, 30),
            date.setUp(2019, 6, 10).subtractDays(10)
        )
    }

    @Test
    fun weeksPassed() {
        val week1 = Date().setUp(2019, 6, 10)
        val week1Sun = Date().setUp(2019, 6, 7)
        val week2 = Date().setUp(2019, 6, 17)
        val week7 = Date().setUp(2019, 7, 24)

        assertEquals(0, week1.weeksPassed(week1Sun))
        assertEquals(-1, week1.weeksPassed(week2))
        assertEquals(6, week7.weeksPassed(week1))
    }

    @Test
    fun getYearOfWeek() {
        val date = Date()
        assertEquals(2019, date.setUp(2019, 6, 10).yearOfWeek)
        assertEquals(2020, date.setUp(2020, 0, 1).yearOfWeek)
        assertEquals(2020, date.setUp(2019, 11, 31).yearOfWeek)
    }
}