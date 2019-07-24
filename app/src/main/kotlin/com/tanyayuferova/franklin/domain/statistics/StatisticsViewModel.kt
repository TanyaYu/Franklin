package com.tanyayuferova.franklin.domain.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tanyayuferova.franklin.R
import com.tanyayuferova.franklin.data.ResourceManager
import com.tanyayuferova.franklin.data.virtue.VirtueRepository
import com.tanyayuferova.franklin.utils.*
import java.util.Date

/**
 * Author: Tanya Yuferova
 * Date: 7/8/19
 */
class StatisticsViewModel(
    private val virtueRepository: VirtueRepository,
    private val resourceManager: ResourceManager,
    private val dateFormatter: DateFormatter,
    private val date: Date
) : ViewModel() {

    private val startDate = MutableLiveData(date)
    val virtues: LiveData<List<Any>>
    val period: LiveData<Pair<Date, Date>>
    val toolbarTitle: LiveData<String>

    init {
        period = startDate.map { start ->
            start to start.addDays(7)
        }

        toolbarTitle = period.map { (start, end) ->
            resourceManager.getString(
                R.string.period_pattern,
                dateFormatter.formatDateShort(start),
                dateFormatter.formatDateShort(end.subtractDays(1))
            )
        }

        virtues = startDate.switchMap { date ->
            virtueRepository.getWeekStatistics(date)
        }
            .distinctUntilChanged()
            .map { list ->
                val sorted = list.sortedByDescending(VirtueStatistics::isSelected)
                mutableListOf<Any>().apply {
                    addAll(sorted)
                    add(0, resourceManager.getString(R.string.virtue_of_the_week))
                    add(2, resourceManager.getString(R.string.other_virtues))
                }.toList()
            }
    }

    fun onPreviousWeekSelected() {
        startDate.value = startDate.requireValue.subtractDays(7)
    }

    fun onNextWeekSelected() {
        startDate.value = startDate.requireValue.addDays(7)
    }
}