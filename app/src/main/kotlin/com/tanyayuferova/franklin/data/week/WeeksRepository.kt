package com.tanyayuferova.franklin.data.week

import androidx.lifecycle.LiveData
import com.tanyayuferova.franklin.data.virtue.VirtueRepository.Companion.FIRST_VIRTUE_ID
import com.tanyayuferova.franklin.data.virtue.VirtueRepository.Companion.VIRTUES_COUNT
import com.tanyayuferova.franklin.utils.map
import com.tanyayuferova.franklin.utils.switchMap
import com.tanyayuferova.franklin.utils.toLiveData
import com.tanyayuferova.franklin.utils.weeksPassed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Author: Tanya Yuferova
 * Date: 7/7/2019
 */
class WeeksRepository private constructor(
    private val weeksDao: WeeksDao
) {
    fun getVirtueOfTheWeek(week: Week): LiveData<Int> {
        return weeksDao.getVirtueByWeek(week.number, week.year)
            .switchMap { list ->
                if (list.isEmpty()) {
                    findPrevious(week).switchMap { previous ->
                        previous?.toLiveData()
                            ?: findNext(week).map { next ->
                                next ?: FIRST_VIRTUE_ID
                            }
                    }
                } else {
                    list.first().toLiveData()
                }
            }
    }

    private fun findNext(week: Week): LiveData<Int?> {
        return weeksDao.getNextExisting(week.number, week.year).map { list ->
            if (list.isEmpty()) null
            else {
                val item = list.first()
                val next = item.toDate()
                val current = week.toDate()
                val weeks = current.weeksPassed(next)
                val newId = (item.virtueId + weeks) % VIRTUES_COUNT
                return@map if (newId == 0) VIRTUES_COUNT else newId
            }
        }
    }

    private fun findPrevious(week: Week): LiveData<Int?> {
        return weeksDao.getPreviousExisting(week.number, week.year).map { list ->
            if (list.isEmpty()) null
            else {
                val item = list.first()
                val last = item.toDate()
                val current = week.toDate()
                val weeks = current.weeksPassed(last)
                val newId = (item.virtueId + weeks) % VIRTUES_COUNT
                return@map if (newId == 0) VIRTUES_COUNT else newId
            }
        }
    }


    suspend fun setVirtueOfTheWeek(virtueId: Int, week: Week) {
        withContext(Dispatchers.IO) {
            week
                .toEntity(virtueId)
                .let(weeksDao::insert)
        }
    }

    companion object {
        @Volatile
        private var instance: WeeksRepository? = null

        fun getInstance(weeksDao: WeeksDao) =
            instance ?: synchronized(this) {
                instance
                    ?: WeeksRepository(weeksDao).also { instance = it }
            }
    }
}