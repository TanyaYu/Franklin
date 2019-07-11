package com.tanyayuferova.franklin.data

import com.tanyayuferova.franklin.data.virtue.VirtueWithPointsEntity
import com.tanyayuferova.franklin.domain.statistics.StatisticResult.*
import com.tanyayuferova.franklin.domain.statistics.StatisticResult.Companion.fromProgress
import com.tanyayuferova.franklin.domain.statistics.VirtueStatistics
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Author: Tanya Yuferova
 * Date: 7/11/19
 */
class VirtueStatisticsTest {

    @Test
    fun virtueWithPointsEntityToVirtueStatistics() {
        val entity = VirtueWithPointsEntity(
            id = 1,
            pointsCount = 4
        )
        val domain = VirtueStatistics(
            id = 1,
            name = "name",
            description = "description",
            pointsCount = 4,
            result = POSITIVE,
            isSelected = true
        )
        assertEquals(domain, entity.toVirtueStatistics("name", "description", 5, true))
    }

    @Test
    fun statisticResultFromValue() {
        assertEquals(POSITIVE, fromProgress(-2))
        assertEquals(NEGATIVE, fromProgress(4))
        assertEquals(NEUTRAL, fromProgress(0))
    }
}