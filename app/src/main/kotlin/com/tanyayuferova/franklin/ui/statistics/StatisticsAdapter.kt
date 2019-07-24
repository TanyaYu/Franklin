package com.tanyayuferova.franklin.ui.statistics

import androidx.recyclerview.widget.DiffUtil
import com.tanyayuferova.franklin.domain.statistics.VirtueStatistics
import com.tanyayuferova.franklin.ui.common.adapter.BaseAdapter

/**
 * Author: Tanya Yuferova
 * Date: 7/14/2019
 */
class StatisticsAdapter : BaseAdapter<Any>(
    delegates = listOf(
        StatisticAdapterDelegate(),
        SubtitleAdapterDelegate()
    ),
    diffItemCallback = object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(old: Any, new: Any): Boolean {
            return when {
                old is String && new is String -> old == new
                old is VirtueStatistics && new is VirtueStatistics -> old.id == new.id
                else -> false
            }
        }

        override fun areContentsTheSame(old: Any, new: Any): Boolean {
            return old == new
        }
    }
)