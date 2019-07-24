package com.tanyayuferova.franklin.domain.statistics

import androidx.annotation.DrawableRes

/**
 * Author: Tanya Yuferova
 * Date: 7/8/19
 */
data class VirtueStatistics(
    val id: Int,
    val name: String,
    val description: String,
    @DrawableRes
    val iconRes: Int,
    val pointsCount: Int,
    val result: StatisticResult,
    val isSelected: Boolean
)