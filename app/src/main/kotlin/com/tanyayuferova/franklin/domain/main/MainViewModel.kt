package com.tanyayuferova.franklin.domain.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val weeksRepository: WeeksRepository
) : ViewModel() {

    val virtues: LiveData<List<Virtue>>
    private val selectedDateMutable = MutableLiveData(today)
    val selectedDate = selectedDateMutable.hide().distinctUntilChanged()

    init {
        virtues = selectedDate
            .switchMap { date ->
                virtueRepository.getVirtuesForDate(date)
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

    @ExperimentalCoroutinesApi
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}