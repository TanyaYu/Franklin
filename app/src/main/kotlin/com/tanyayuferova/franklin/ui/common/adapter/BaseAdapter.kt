package com.tanyayuferova.franklin.ui.common.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.lang.IllegalStateException

/**
 * Author: Tanya Yuferova
 * Date: 7/11/2019
 */
open class BaseAdapter<T : Any>(
    private val delegates: List<AdapterDelegate<T, *, *>>,
    private val diffItemCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, RecyclerView.ViewHolder>(diffItemCallback) {

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        delegates.forEachIndexed { index, delegate ->
            if(delegate.isForViewType(item)) {
                return index
            }
        }
        throw IllegalStateException("No delegate for item $item")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates[viewType].onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        return delegates[getItemViewType(position)].onBindViewHolder(item, holder)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val item = getItem(position)
        return delegates[getItemViewType(position)].onBindViewHolder(item, holder, payloads)
    }
}