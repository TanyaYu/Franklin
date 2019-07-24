package com.tanyayuferova.franklin.ui.main

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.tanyayuferova.franklin.ui.views.DaySelectorView
import com.tanyayuferova.franklin.utils.addDays
import com.tanyayuferova.franklin.utils.getFirstDayOfWeek
import com.tanyayuferova.franklin.utils.today
import java.util.*



/**
 * Author: Tanya Yuferova
 * Date: 7/12/19
 */
class WeeksSliderAdapter(
    private val count: Int
) : PagerAdapter() {

    var selectedDate: Date = today
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onDayClickListener: (Date) -> Unit = { }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return DaySelectorView(container.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            firstDay = getFirstDayOfWeek(today).addDays((position - count / 2) * 7)
            selectedDay = this@WeeksSliderAdapter.selectedDate
            this.onDayClickListener = this@WeeksSliderAdapter.onDayClickListener

            container.addView(this)
        }
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return `object` == view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun getCount() = count
}