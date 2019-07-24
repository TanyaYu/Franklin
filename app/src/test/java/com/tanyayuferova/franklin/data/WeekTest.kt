package com.tanyayuferova.franklin.data

import com.tanyayuferova.franklin.data.week.Week
import com.tanyayuferova.franklin.data.week.WeekEntity
import com.tanyayuferova.franklin.data.week.toWeek
import com.tanyayuferova.franklin.utils.removeTime
import com.tanyayuferova.franklin.utils.setUp
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

/**
 * Author: Tanya Yuferova
 * Date: 7/11/19
 */
class WeekTest {

    @Test
    fun weekEntityToDate() {
        val entity = WeekEntity(
            year = 2019,
            week = 28,
            virtueId = 2
        )
        assertEquals(
            Date().setUp(2019, 6, 7, 0, 0, 0, 0),
            entity.toDate().removeTime()
        )
    }

    @Test
    fun weekToDate() {
        val entity = Week(year = 2019, number = 28)
        println(Week(year = 2019, number = 29).toDate())
        assertEquals(
            Date().setUp(2019, 6, 7, 0, 0, 0, 0),
            entity.toDate().removeTime()
        )
        assertEquals(
            Date().setUp(2019, 6, 14, 0, 0, 0, 0),
            Week(year = 2019, number = 29).toDate().removeTime()
        )
    }

    @Test
    fun dateToWeek() {
        val entity = Week(year = 2019, number = 28)
        assertEquals(
            entity,
            Date().setUp(2019, 6, 7).toWeek()
        )
    }
}