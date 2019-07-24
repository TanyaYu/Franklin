package com.tanyayuferova.franklin.ui.statistics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tanyayuferova.franklin.R
import com.tanyayuferova.franklin.ui.common.adapter.AdapterDelegate

/**
 * Author: Tanya Yuferova
 * Date: 7/14/2019
 */
class SubtitleAdapterDelegate : AdapterDelegate<Any, String, SubtitleAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any) = item is String

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_subtitle,
                parent,
                false
            )
        )
    }

    override fun onBind(item: String, viewHolder: ViewHolder, payload: List<Any>) {
        viewHolder.subtitle.text = item
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val subtitle: TextView = view as TextView
    }
}