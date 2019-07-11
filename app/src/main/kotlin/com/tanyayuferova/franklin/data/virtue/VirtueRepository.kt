package com.tanyayuferova.franklin.data.virtue

import androidx.lifecycle.LiveData
import com.tanyayuferova.franklin.data.week.WeeksRepository
import com.tanyayuferova.franklin.data.week.toWeek
import com.tanyayuferova.franklin.domain.main.Virtue
import com.tanyayuferova.franklin.domain.statistics.VirtueStatistics
import com.tanyayuferova.franklin.utils.*
import java.util.Date

/**
 * Author: Tanya Yuferova
 * Date: 7/7/19
 */
class VirtueRepository private constructor(
    private val virtueDao: VirtueDao,
    private val weekRepository: WeeksRepository
) {

    private fun getVirtuesForPeriod(start: Date, end: Date) = virtueDao.getWithPointsForPeriod(start, end)

    fun getVirtuesForDate(date: Date): LiveData<List<Virtue>> {
        return weekRepository.getVirtueOfTheWeek(date.toWeek())
            .switchMap { selectedId ->
                virtueDao.getWithPointsForDate(date.removeTime())
                    .mapList { entity ->
                        val id = entity.id
                        entity.toVirtue(
                            name = "name$id",
                            description = "description$id",
                            isSelected = id == selectedId
                        )
                    }
            }
    }

    fun getWeekStatistics(date: Date): LiveData<List<VirtueStatistics>> {
        val startDate = getFirstDayOfWeek(date)
        return getVirtueStatistics(startDate, 7)
    }

    private fun getVirtueStatistics(startDate: Date, days: Int): LiveData<List<VirtueStatistics>> {
        val endDate = startDate.addDays(days)
        val previousStartDate = startDate.subtractDays(days)
        val previousEndDate = startDate
        return weekRepository.getVirtueOfTheWeek(startDate.toWeek()).switchMap { selectedId ->
            getVirtuesForPeriod(startDate, endDate).switchMap { current ->
                getVirtuesForPeriod(previousStartDate, previousEndDate).map { previous ->
                    current.map { entity ->
                        val id = entity.id
                        entity.toVirtueStatistics(
                            name = "name$id",
                            description = "description$id",
                            isSelected = selectedId == id,
                            previousPoints = previous.find { it.id == id }?.pointsCount ?: 0
                        )
                    }
                }
            }
        }
    }

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: VirtueRepository? = null

        fun getInstance(virtueDao: VirtueDao, weekRepository: WeeksRepository) =
            instance ?: synchronized(this) {
                instance
                    ?: VirtueRepository(virtueDao, weekRepository).also { instance = it }
            }

        const val FIRST_VIRTUE_ID = 1
        const val VIRTUES_COUNT = 13
        val virtueInitialValues = (1..VIRTUES_COUNT).map { VirtueEntity(it) }
    }
}