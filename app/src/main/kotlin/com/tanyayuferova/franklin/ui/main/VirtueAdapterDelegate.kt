package com.tanyayuferova.franklin.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tanyayuferova.franklin.R
import com.tanyayuferova.franklin.domain.main.Virtue

/**
 * Author: Tanya Yuferova
 * Date: 7/11/19
 */
class VirtueAdapterDelegate(callback: Callback? = null) : BaseVirtueAdapterDelegate<VirtueAdapterDelegate.ViewHolder>(callback) {

    override fun isForViewType(item: Virtue) = !item.isSelected

    override fun onCreateViewHolder(parent: ViewGroup) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_virtue, parent, false)
    )

    class ViewHolder(view: View) : BaseVirtueAdapterDelegate.ViewHolder(view)
}