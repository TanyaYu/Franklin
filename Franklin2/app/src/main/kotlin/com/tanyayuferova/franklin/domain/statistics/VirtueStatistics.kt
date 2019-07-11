package com.tanyayuferova.franklin.domain.statistics

/**
 * Author: Tanya Yuferova
 * Date: 7/8/19
 */
data class VirtueStatistics(
    val id: Int,
    val name: String,
    val description: String,
    val pointsCount: Int,
    val result: StatisticResult,
    val isSelected: Boolean
)