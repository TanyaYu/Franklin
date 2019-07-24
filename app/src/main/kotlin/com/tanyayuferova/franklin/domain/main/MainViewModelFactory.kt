package com.tanyayuferova.franklin.domain.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tanyayuferova.franklin.data.ResourceManager
import com.tanyayuferova.franklin.data.point.PointsRepository
import com.tanyayuferova.franklin.data.virtue.VirtueRepository
import com.tanyayuferova.franklin.data.week.WeeksRepository
import com.tanyayuferova.franklin.utils.DateFormatter

/**
 * Author: Tanya Yuferova
 * Date: 7/7/19
 */
class MainViewModelFactory(
    private val virtueRepository: VirtueRepository,
    private val pointsRepository: PointsRepository,
    private val weeksRepository: WeeksRepository,
    private val resourceManager: ResourceManager,
    private val dateFormatter: DateFormatter
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  MainViewModel(
            virtueRepository, pointsRepository, weeksRepository,
            resourceManager, dateFormatter
        ) as T
    }
}