package com.tanyayuferova.franklin.domain.statistics

/**
 * Author: Tanya Yuferova
 * Date: 7/8/19
 */
enum class StatisticResult {
    POSITIVE,
    NEGATIVE,
    NEUTRAL;

    companion object {
        fun fromProgress(progress: Int) = when {
            progress < 0 -> POSITIVE
            progress > 0 -> NEGATIVE
            else -> NEUTRAL
        }
    }
}