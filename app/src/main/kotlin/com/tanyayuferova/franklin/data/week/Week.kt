package com.tanyayuferova.franklin.data.week

import com.tanyayuferova.franklin.utils.getFirstDayOfWeek
import com.tanyayuferova.franklin.utils.week
import com.tanyayuferova.franklin.utils.yearOfWeek
import java.util.Date

/**
 * Author: Tanya Yuferova
 * Date: 7/9/19
 */
data class Week(
    val number: Int,
    val year: Int
) {
    fun toEntity(virtueId: Int) = WeekEntity(
        week = number,
        year = year,
        virtueId = virtueId
    )

    fun toDate() = getFirstDayOfWeek(year, number)
}

fun Date.toWeek() = Week(week, yearOfWeek)