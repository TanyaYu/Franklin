package com.tanyayuferova.franklin.data.virtue

import androidx.lifecycle.LiveData
import com.tanyayuferova.franklin.R
import com.tanyayuferova.franklin.data.ResourceManager
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
    private val weekRepository: WeeksRepository,
    private val resourceManager: ResourceManager
) {

    fun getVirtuesForDate(date: Date): LiveData<List<Virtue>> {
        return weekRepository.getVirtueOfTheWeek(date.toWeek())
            .switchMap { selectedId ->
                virtueDao.getWithPointsForDate(date.removeTime())
                    .mapList { entity ->
                        val id = entity.id
                        entity.toVirtue(
                            name = getVirtueName(id),
                            description = getVirtueDescription(id),
                            iconRes = getVirtueIconRes(id),
                            isSelected = id == selectedId
                        )
                    }
            }
    }

    fun getWeekStatistics(startDate: Date): LiveData<List<VirtueStatistics>> {
        return getVirtueStatistics(startDate, 7)
    }

    private fun getVirtuesForPeriod(start: Date, end: Date) =
        virtueDao.getWithPointsForPeriod(start, end)

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
                            name = getVirtueName(id),
                            description = getVirtueDescription(id),
                            iconRes = getVirtueIconRes(id),
                            isSelected = selectedId == id,
                            previousPoints = previous.find { it.id == id }?.pointsCount ?: 0
                        )
                    }
                }
            }
        }
    }

    private fun getVirtueName(id: Int): String {
        return resourceManager.getStringArray(R.array.virtues_names)[id - 1]
    }

    private fun getVirtueDescription(id: Int): String {
        return resourceManager.getStringArray(R.array.virtues_descriptions)[id - 1]
    }

    private fun getVirtueIconRes(id: Int): Int {
        return when (id) {
            1 -> R.drawable.tem_icon
            2 -> R.drawable.sil_icon
            3 -> R.drawable.ord_icon
            4 -> R.drawable.res_icon
            5 -> R.drawable.fru_icon
            6 -> R.drawable.ind_icon
            7 -> R.drawable.sin_icon
            8 -> R.drawable.jus_icon
            9 -> R.drawable.mod_icon
            10 -> R.drawable.cle_icon
            11 -> R.drawable.tra_icon
            12 -> R.drawable.cha_icon
            13 -> R.drawable.hum_icon
            else -> 0
        }
    }

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: VirtueRepository? = null

        fun getInstance(
            virtueDao: VirtueDao,
            weekRepository: WeeksRepository,
            resourceManager: ResourceManager
        ) =
            instance ?: synchronized(this) {
                instance
                    ?: VirtueRepository(
                        virtueDao,
                        weekRepository,
                        resourceManager
                    ).also { instance = it }
            }

        const val FIRST_VIRTUE_ID = 1
        const val VIRTUES_COUNT = 13
        val virtueInitialValues =
            (1..VIRTUES_COUNT).map { VirtueEntity(it) } //todo same in arrays???
    }
}