package com.tanyayuferova.franklin.ui.main

import androidx.recyclerview.widget.DiffUtil
import com.tanyayuferova.franklin.domain.main.Virtue
import com.tanyayuferova.franklin.ui.common.adapter.BaseAdapter
import com.tanyayuferova.franklin.ui.main.BaseVirtueAdapterDelegate.Callback


/**
 * Author: Tanya Yuferova
 * Date: 7/11/2019
 */
class VirtuesAdapter(callback: Callback? = null) : BaseAdapter<Virtue>(
    delegates = listOf(
        VirtueAdapterDelegate(callback),
        SelectedVirtueAdapterDelegate(callback)
    ),
    diffItemCallback = object : DiffUtil.ItemCallback<Virtue>() {
        override fun areItemsTheSame(old: Virtue, new: Virtue): Boolean {
            return old.id == new.id
        }

        override fun areContentsTheSame(old: Virtue, new: Virtue): Boolean {
            return old == new
        }

        override fun getChangePayload(old: Virtue, new: Virtue): Any? {
            return when {
                old.points != new.points -> POINTS_PAYLOAD_KEY
                else -> null
            }
        }
    }
) {
    companion object {
        const val POINTS_PAYLOAD_KEY = "POINTS_PAYLOAD_KEY"
    }
}
