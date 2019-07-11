package com.tanyayuferova.franklin.domain.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tanyayuferova.franklin.data.virtue.VirtueRepository
import com.tanyayuferova.franklin.utils.*
import java.util.Date

/**
 * Author: Tanya Yuferova
 * Date: 7/8/19
 */
class StatisticsViewModel(
    private val virtueRepository: VirtueRepository,
    private val date: Date
) : ViewModel() {

    val virtues: LiveData<List<VirtueStatistics>>
    private val selectedDateMutable = MutableLiveData<Date>()
    val selectedDate = selectedDateMutable.distinctUntilChanged()

    init {
        selectedDateMutable.value = date
        virtues = selectedDate.switchMap { date ->
            virtueRepository.getWeekStatistics(date)
                .map { list ->
                    list.sortedByDescending(VirtueStatistics::isSelected)
                }
                .distinctUntilChanged()
        }
    }

    fun onPreviousWeekSelected() {
        selectedDateMutable.value = selectedDate.requireValue.subtractDays(7)
    }

    fun onNextWeekSelected() {
        selectedDateMutable.value = selectedDate.requireValue.addDays(7)
    }
}