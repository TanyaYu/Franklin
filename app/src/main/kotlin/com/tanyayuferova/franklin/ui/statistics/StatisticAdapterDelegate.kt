package com.tanyayuferova.franklin.ui.statistics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.tanyayuferova.franklin.R
import com.tanyayuferova.franklin.domain.statistics.StatisticResult.*
import com.tanyayuferova.franklin.domain.statistics.VirtueStatistics
import com.tanyayuferova.franklin.ui.common.adapter.AdapterDelegate
import com.tanyayuferova.franklin.utils.getColorCompat

/**
 * Author: Tanya Yuferova
 * Date: 7/14/2019
 */
class StatisticAdapterDelegate :
    AdapterDelegate<Any, VirtueStatistics, StatisticAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any) = item is VirtueStatistics

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_virtue_result,
                parent,
                false
            )
        )
    }

    override fun onBind(item: VirtueStatistics, viewHolder: ViewHolder, payload: List<Any>) =
        with(viewHolder) {
            val context = itemView.context
            icon.setImageResource(item.iconRes)
            name.text = item.name
            dots.text = item.pointsCount.toString()
            val progressColorRes = when (item.result) {
                POSITIVE -> R.color.result_positive
                NEGATIVE -> R.color.result_negative
                NEUTRAL -> R.color.result_neutral
            }
            val progressIconRes = when (item.result) {
                POSITIVE -> R.drawable.ic_arrow_up
                NEGATIVE -> R.drawable.ic_arrow_down
                NEUTRAL -> null
            }
            val progressColor = context.getColorCompat(progressColorRes)
            val progressDrawable = progressIconRes
                ?.let { ContextCompat.getDrawable(context, it) }
                ?.let { drawable ->
                    DrawableCompat.setTint(drawable, progressColor)
                    return@let drawable
                }
            iconProgress.setImageDrawable(progressDrawable)
            dots.setTextColor(progressColor)
        }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.icon)
        val name: TextView = view.findViewById(R.id.name)
        val dots: TextView = view.findViewById(R.id.dots)
        val iconProgress: ImageView = view.findViewById(R.id.icon_progress)
    }
}