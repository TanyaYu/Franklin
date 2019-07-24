package com.tanyayuferova.franklin.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tanyayuferova.franklin.R
import com.tanyayuferova.franklin.domain.main.Virtue

/**
 * Author: Tanya Yuferova
 * Date: 7/12/19
 */
class SelectedVirtueAdapterDelegate(callback: Callback? = null) : BaseVirtueAdapterDelegate<SelectedVirtueAdapterDelegate.ViewHolder>(callback) {

    override fun isForViewType(item: Virtue) = item.isSelected

    override fun onCreateViewHolder(parent: ViewGroup) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_virtue_selected, parent, false)
    )

    override fun onBind(item: Virtue, viewHolder: ViewHolder, payload: List<Any>) {
        super.onBind(item, viewHolder, payload)
        viewHolder.description.text = item.description
    }

    class ViewHolder(view: View) : BaseVirtueAdapterDelegate.ViewHolder(view) {
        val description: TextView = view.findViewById(R.id.description)
    }
}