package com.tanyayuferova.franklin.domain.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanyayuferova.franklin.R
import com.tanyayuferova.franklin.data.ResourceManager
import com.tanyayuferova.franklin.data.point.PointsRepository
import com.tanyayuferova.franklin.data.virtue.VirtueRepository
import com.tanyayuferova.franklin.data.week.WeeksRepository
import com.tanyayuferova.franklin.data.week.toWeek
import com.tanyayuferova.franklin.utils.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.Date

/**
 * Author: Tanya Yuferova
 * Date: 7/7/19
 */
class MainViewModel(
    private val virtueRepository: VirtueRepository,
    private val pointsRepository: PointsRepository,
    private val weeksRepository: WeeksRepository,
    private val resourceManager: ResourceManager,
    private val dateFormatter: DateFormatter
) : ViewModel() {

    private val selectedDateMutable = MutableLiveData(today)
    private val toolbarTitleMutable = MutableLiveData<String>()
    val virtues: LiveData<List<Virtue>>
    val selectedDate: LiveData<Date> = selectedDateMutable.distinctUntilChanged()
    val toolbarTitle: LiveData<String> = toolbarTitleMutable.distinctUntilChanged()

    init {
        virtues = selectedDate
            .switchMap { date ->
                virtueRepository.getVirtuesForDate(date)
            }
            .map { list ->
                list.sortedBy(Virtue::id)
            }
            .distinctUntilChanged()
    }

    fun onSelectedDateChanged(date: Date) {
        selectedDateMutable.value = date
    }

    fun onAddPoint(virtueId: Int) {
        viewModelScope.launch {
            pointsRepository.addPoint(virtueId, selectedDate.requireValue)
        }
    }

    fun onRemovePoint(virtueId: Int) {
        viewModelScope.launch {
            pointsRepository.removePoint(virtueId, selectedDate.requireValue)
        }
    }

    fun onVirtueSelected(id: Int) {
        viewModelScope.launch {
            weeksRepository.setVirtueOfTheWeek(id, selectedDate.requireValue.toWeek())
        }
    }

    fun onWeekSliderPageChanged(pageNumber: Int) {
        val start = getFirstDayOfWeek(today).addDays(7 * pageNumber)
        val end = start.addDays(7)
        toolbarTitleMutable.value = if (start.month_ == end.month_) {
            dateFormatter.formatMonth(start)
        } else {
            resourceManager.getString(
                R.string.period_pattern,
                dateFormatter.formatMonth(start),
                dateFormatter.formatMonth(end)
            )
        }
    }

    @ExperimentalCoroutinesApi
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}