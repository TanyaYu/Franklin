package com.tanyayuferova.franklin.data.virtue

import androidx.room.ColumnInfo
import com.tanyayuferova.franklin.domain.main.Virtue
import com.tanyayuferova.franklin.domain.statistics.StatisticResult
import com.tanyayuferova.franklin.domain.statistics.VirtueStatistics

/**
 * Author: Tanya Yuferova
 * Date: 7/7/2019
 */
data class VirtueWithPointsEntity(
    val id: Int,
    @ColumnInfo(name = "points_count")
    var pointsCount: Int
) {

    fun toVirtue(
        name: String,
        description: String,
        isSelected: Boolean
    ) = Virtue(
        id = id,
        name = name,
        description = description,
        points = pointsCount,
        isSelected = isSelected
    )

    fun toVirtueStatistics(
        name: String,
        description: String,
        previousPoints: Int,
        isSelected: Boolean
    ) = VirtueStatistics(
        id = id,
        name = name,
        description = description,
        pointsCount = pointsCount,
        result = StatisticResult.fromProgress(pointsCount - previousPoints),
        isSelected = isSelected
    )
}