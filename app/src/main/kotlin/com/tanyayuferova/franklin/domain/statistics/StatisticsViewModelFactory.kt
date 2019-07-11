package com.tanyayuferova.franklin.domain.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tanyayuferova.franklin.data.virtue.VirtueRepository
import java.util.Date

/**
 * Author: Tanya Yuferova
 * Date: 7/8/19
 */
class StatisticsViewModelFactory(
    private val virtueRepository: VirtueRepository,
    private val date: Date
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StatisticsViewModel(
            virtueRepository,
            date
        ) as T
    }
}