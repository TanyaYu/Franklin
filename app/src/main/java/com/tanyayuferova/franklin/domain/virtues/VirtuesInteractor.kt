package com.tanyayuferova.franklin.domain.virtues

import android.content.ContentValues
import android.content.res.Resources
import android.support.v4.content.CursorLoader
import com.tanyayuferova.franklin.R
import com.tanyayuferova.franklin.data.VirtuesContract.*
import com.tanyayuferova.franklin.data.virtue.VirtueRepository
import com.tanyayuferova.franklin.domain.extentions.addDays
import com.tanyayuferova.franklin.domain.extentions.toCalendar
import com.tanyayuferova.franklin.data.virtue.Virtue
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Author: Tanya Yuferova
 * Date: 6/26/2018
 */
class VirtuesInteractor @Inject constructor(
    private val virtueRepository: VirtueRepository,
    private val resources: Resources
) {
    fun addMark(virtueId: Long, date: Date){
        virtueRepository.insert(buildPointsUriWithDate(virtueId, date))
    }

    fun removeMark(virtueId: Long, date: Date){
        virtueRepository.delete(buildPointsUriWithDate(virtueId, date))
    }

    fun getVirtuesMarksForDay(date: Date): CursorLoader {
        val formattedDateString = SimpleDateFormat("yyyyMMdd").format(date)
        val projection = arrayOf(
            VirtueEntry._ID,
            """ (
                select count(*)
                from ${PointEntry.TABLE_NAME}
                    where ${VirtueEntry.TABLE_NAME}.${VirtueEntry._ID} = ${PointEntry.TABLE_NAME}.${PointEntry.COLUMN_VIRTUE_ID}
                    and ${PointEntry.TABLE_NAME}.${PointEntry.COLUMN_DATE}='$formattedDateString'
            ) as 'day' """
        )
        val sortOrder = VirtueEntry._ID + " ASC"
        return virtueRepository.virtuesCursorLoader(
            projection = projection,
            sortOrder = sortOrder
        )
    }

    fun getResults(currentDate: Date): CursorLoader {
        val dateFormat = SimpleDateFormat("yyyyMMdd")
        val sortOrder = VirtueEntry._ID + " ASC"
        val projection = arrayOf(
            VirtueEntry._ID,
            """ (
                select count(*)
                from ${PointEntry.TABLE_NAME}
                where ${VirtueEntry.TABLE_NAME}.${VirtueEntry._ID} = ${PointEntry.TABLE_NAME}.${PointEntry.COLUMN_VIRTUE_ID}
                    and ${PointEntry.TABLE_NAME}.${PointEntry.COLUMN_DATE}
                        between '${dateFormat.format(currentDate)}' and '${dateFormat.format(currentDate.addDays(6))}'
            ) as 'current_week'""",
            """ (
                select count(*)
                from ${PointEntry.TABLE_NAME}
                where ${VirtueEntry.TABLE_NAME}.${VirtueEntry._ID} = ${PointEntry.TABLE_NAME}.${PointEntry.COLUMN_VIRTUE_ID}
                and ${PointEntry.TABLE_NAME}.${PointEntry.COLUMN_DATE}
                    between '${dateFormat.format(currentDate.addDays(-7))}' and ' ${dateFormat.format(currentDate.addDays(-1))}'
            ) as 'prev_week'"""
        )
        return virtueRepository.virtuesCursorLoader(
            projection = projection,
            sortOrder = sortOrder
        )
    }

    fun getVirtueIdOfWeek(date: Date): Int {
        val calendar = date.toCalendar()
        val week = calendar.get(Calendar.WEEK_OF_YEAR)
        // This is necessary for the last week of the year, which is the first week of the next year also
        //todo think to move
        val year = if (week == 1 && calendar.get(Calendar.MONTH) == Calendar.DECEMBER)
            calendar.get(Calendar.YEAR) + 1
        else calendar.get(Calendar.YEAR)

        val cursor = virtueRepository.query(
            uri = buildVirtueUriWithWeek(week, year),
            projection = arrayOf(WeekEntry.COLUMN_VIRTUE_ID)
        )
        if (cursor.moveToFirst()) {
            val result = cursor.getInt(0)
            cursor.close()
            return result
        }

        val result = getNextVirtueId(week, year)
            ?: getPreviousId(week, year)
            ?: virtuesIds.first()
        setVirtueOfWeek(result, week, year)

        return result
    }

    private fun getNextVirtueId(week: Int, year: Int): Int? {
        val cursor = virtueRepository.query(
            uri = CONTENT_WEEKS_URI,
            selection = """${WeekEntry.COLUMN_YEAR} = ?
                and ${WeekEntry.COLUMN_WEEK} < ?
                or ${WeekEntry.COLUMN_YEAR} < ?""",
            selectionArgs = arrayOf(year.toString(), week.toString(), year.toString()),
            sortOrder = "${WeekEntry.COLUMN_YEAR} DESC, ${WeekEntry.COLUMN_WEEK} DESC"
        )
        if (cursor.moveToFirst()) {
            /* The last id in history and its week number and year */
            val lastId = cursor.getInt(cursor.getColumnIndex(WeekEntry.COLUMN_VIRTUE_ID))
            val lastWeek = cursor.getInt(cursor.getColumnIndex(WeekEntry.COLUMN_WEEK))
            val lastYear = cursor.getInt(cursor.getColumnIndex(WeekEntry.COLUMN_YEAR))
            cursor.close()

            val last = Calendar.getInstance().apply {
                set(Calendar.YEAR, lastYear)
                set(Calendar.WEEK_OF_YEAR, lastWeek)
            }

            val current = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.WEEK_OF_YEAR, week)
            }

            /* How many weeks passed after last entry */
            val weeks = ((current.time.time - last.time.time) / MILLIS_IN_WEEK).toInt()

            val virtuesAmount = virtuesIds.size
            val newId = (lastId + weeks) % virtuesAmount
            return if (newId == 0) virtuesAmount else newId
        }
        return null
    }

    private fun getPreviousId(week: Int, year: Int): Int? {
        val cursor = virtueRepository.query(
            uri = CONTENT_WEEKS_URI,
            selection = """${WeekEntry.COLUMN_YEAR} = ?
                and ${WeekEntry.COLUMN_WEEK} > ?
                or ${WeekEntry.COLUMN_YEAR} > ?""",
            selectionArgs = arrayOf(year.toString(), week.toString(), year.toString()),
            sortOrder = "${WeekEntry.COLUMN_YEAR} ASC, ${WeekEntry.COLUMN_WEEK} ASC"
        )
        if (cursor.moveToFirst()) {
            /* The first id in history and its week number and year */
            val lastId = cursor.getInt(cursor.getColumnIndex(WeekEntry.COLUMN_VIRTUE_ID))
            val lastWeek = cursor.getInt(cursor.getColumnIndex(WeekEntry.COLUMN_WEEK))
            val lastYear = cursor.getInt(cursor.getColumnIndex(WeekEntry.COLUMN_YEAR))
            cursor.close()

            val last = Calendar.getInstance().apply {
                set(Calendar.YEAR, lastYear)
                set(Calendar.WEEK_OF_YEAR, lastWeek)
            }

            val current = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.WEEK_OF_YEAR, week)
            }

            /* How many weeks passed before first entry */
            val weeks = ((last.time.time - current.time.time) / MILLIS_IN_WEEK).toInt()

            val virtuesAmount = virtuesIds.size
            val newId = (lastId - weeks) % virtuesAmount
            return if (newId == 0) virtuesAmount else newId
        }
        return null
    }

    fun setVirtueOfWeek(virtueId: Int, date: Date) {
        val calendar = date.toCalendar()
        val week = calendar.get(Calendar.WEEK_OF_YEAR)
        var year = calendar.get(Calendar.YEAR)

        // This is necessary for the last week of the year, which is the first week of the next year also
        if (week == 1 && calendar.get(Calendar.MONTH) == Calendar.DECEMBER)
            year++

        setVirtueOfWeek(virtueId, week, year)
    }

    fun setVirtueOfWeek(virtueId: Int, week: Int, year: Int) {
        val uri = buildVirtueUriWithWeek(week, year)
        val values = ContentValues().apply {
            put(WeekEntry.COLUMN_VIRTUE_ID, virtueId)
        }
        virtueRepository.delete(uri)
        virtueRepository.insert(uri, values)
    }

    val virtuesIds = resources.getIntArray(R.array.virtues_ids).toTypedArray()

    val virtuesNames = resources.getStringArray(R.array.virtues_names)

    val virtuesShortNames = resources.getStringArray(R.array.virtues_short_names)

    val virtuesDescriptions = resources.getStringArray(R.array.virtues_descriptions)

    val virtueIconsRes: Array<Int>
        get() {
            //todo refactor
            val tArray = resources.obtainTypedArray(R.array.virtues_icons)
            val icons = IntArray(tArray.length())
            for (i in icons.indices) {
                icons[i] = tArray.getResourceId(i, 0)
            }
            tArray.recycle()
            return icons.toTypedArray()
        }

    fun getVirtueById(id: Int): Virtue {
        val index = virtuesIds.indexOf(id)
        return Virtue(
            id,
            virtuesNames[index],
            virtuesShortNames[index],
            virtuesDescriptions[index],
            virtueIconsRes[index]
        )
    }

    companion object {
        private val MILLIS_IN_WEEK = TimeUnit.DAYS.toMillis(7)
    }
}