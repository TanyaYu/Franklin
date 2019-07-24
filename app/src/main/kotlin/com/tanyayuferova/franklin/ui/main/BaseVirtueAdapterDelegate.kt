package com.tanyayuferova.franklin.ui.main

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.tanyayuferova.franklin.R
import com.tanyayuferova.franklin.domain.main.Virtue
import com.tanyayuferova.franklin.ui.common.adapter.AdapterDelegate
import com.tanyayuferova.franklin.ui.main.VirtuesAdapter.Companion.POINTS_PAYLOAD_KEY

/**
 * Author: Tanya Yuferova
 * Date: 7/14/2019
 */
abstract class BaseVirtueAdapterDelegate<VH : BaseVirtueAdapterDelegate.ViewHolder>(
    private val callback: Callback? = null
) : AdapterDelegate<Virtue, Virtue, VH>() {

    abstract override fun isForViewType(item: Virtue): Boolean

    abstract override fun onCreateViewHolder(parent: ViewGroup): VH

    @CallSuper
    override fun onBind(
        item: Virtue,
        viewHolder: VH,
        payload: List<Any>
    ) = with(viewHolder as ViewHolder) {
        dots.text = if (item.points < 4) {
            itemView.context.getString(R.string.dot).repeat(item.points)
        } else {
            item.points.toString()
        }

        if (payload.contains(POINTS_PAYLOAD_KEY)) return@with

        name.text = item.name
        icon.setImageResource(item.iconRes)

        icon.setOnClickListener {
            callback?.onIconClick(item.id)
        }
        itemView.setOnClickListener {
            callback?.onClick(item.id)
        }
        itemView.setOnLongClickListener {
            callback?.onLongClick(item.id)
            true
        }
    }

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
        val dots: TextView = view.findViewById(R.id.dots)
        val icon: ImageView = view.findViewById(R.id.icon)
    }

    interface Callback {
        fun onIconClick(virtueId: Int)
        fun onClick(virtueId: Int)
        fun onLongClick(virtueId: Int)
    }
}