package com.tanyayuferova.franklin.utils

import com.tanyayuferova.franklin.R
import com.tanyayuferova.franklin.data.ResourceManager
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Author: Tanya Yuferova
 * Date: 7/17/19
 */
class DateFormatter(
    private val resourceManager: ResourceManager
) {
    private val dateShort = SimpleDateFormat.getDateInstance(DateFormat.SHORT)
    private val timeShort = SimpleDateFormat.getTimeInstance(DateFormat.SHORT)
    private val month = SimpleDateFormat(resourceManager.getString(R.string.date_format_month))

    fun formatMonth(date: Date) = month.format(date)
    fun formatDateShort(date: Date) = dateShort.format(date)
    fun formatTimeShort(date: Date) = timeShort.format(date)

}
//todo check if leacking