package com.tanyayuferova.franklin.ui.common.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Author: Tanya Yuferova
 * Date: 7/14/2019
 */
abstract class AdapterDelegate<All: Any, T : All, VH : RecyclerView.ViewHolder> {

    abstract fun isForViewType(item: All): Boolean

    abstract fun onCreateViewHolder(parent: ViewGroup): VH

    @Suppress("UNCHECKED_CAST")
    fun onBindViewHolder(item: All, holder: RecyclerView.ViewHolder, payload: List<Any> = emptyList()) {
        onBind(item as T, holder as VH, payload)
    }

    abstract fun onBind(item: T, viewHolder : VH, payload: List<Any>)
}