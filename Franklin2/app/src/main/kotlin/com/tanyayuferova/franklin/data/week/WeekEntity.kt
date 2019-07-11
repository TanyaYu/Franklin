package com.tanyayuferova.franklin.data.week

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.NO_ACTION
import com.tanyayuferova.franklin.data.virtue.VirtueEntity
import com.tanyayuferova.franklin.utils.getFirstDayOfWeek

/**
 * Author: Tanya Yuferova
 * Date: 7/7/2019
 */
@Entity(tableName = "week",
    primaryKeys = ["week", "year"],
    foreignKeys = [
        ForeignKey(
            entity = VirtueEntity::class,
            parentColumns = ["id"],
            childColumns = ["virtue_id"],
            onDelete = NO_ACTION
        )
    ]
)
data class WeekEntity(
    val week: Int,
    val year: Int,
    @ColumnInfo(name = "virtue_id")
    val virtueId: Int
) {
    fun toWeek() = Week(
        number = week,
        year = year
    )

    fun toDate() = getFirstDayOfWeek(year, week)
}