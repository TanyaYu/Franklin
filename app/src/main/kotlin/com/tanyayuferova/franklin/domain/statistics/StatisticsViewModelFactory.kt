package com.tanyayuferova.franklin.domain.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tanyayuferova.franklin.data.ResourceManager
import com.tanyayuferova.franklin.data.virtue.VirtueRepository
import com.tanyayuferova.franklin.utils.DateFormatter
import java.util.Date

/**
 * Author: Tanya Yuferova
 * Date: 7/8/19
 */
class StatisticsViewModelFactory(
    private val virtueRepository: VirtueRepository,
    private val resourceManager: ResourceManager,
    private val dateFormatter: DateFormatter,
    private val date: Date
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StatisticsViewModel(
            virtueRepository, resourceManager, dateFormatter, date
        ) as T
    }
}