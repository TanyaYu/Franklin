package com.tanyayuferova.franklin.ui.views

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

import com.tanyayuferova.franklin.R

import java.text.SimpleDateFormat
import java.util.Date

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.annotation.StyleRes
import androidx.appcompat.view.ContextThemeWrapper
import com.tanyayuferova.franklin.utils.addDays
import com.tanyayuferova.franklin.utils.isSameDay
import com.tanyayuferova.franklin.utils.isToday

/**
 * Created by Tanya Yuferova on 3/17/2018.
 */

class DaySelectorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.daySelectorView
) : LinearLayout(context, attrs, defStyleAttr) {

    var daysCount = 0
        set(value) {
            field = value
            initLayout()
        }

    var firstDay = Date()
        set(firstDate) {
            field = firstDate
            initLayout()
        }

    var selectedDay = Date()
        set(value) {
            field = value
            for (i in 0 until childCount) {
                getChildAt(i).isSelected = i == selectedIndex
            }
        }

    private val selectedIndex: Int
        get() {
            for (index in 0 until daysCount) {
                val d = firstDay.addDays(index)
                if (selectedDay.isSameDay(d)) {
                    return index
                }
            }
            return -1
        }

    private var dateFormat: SimpleDateFormat
    @StyleRes
    private var dayViewStyle: Int

    var onDayClickListener: (Date) -> Unit = {}

    init {
        orientation = HORIZONTAL

        val array = context.theme.obtainStyledAttributes(
            attrs, R.styleable.DaySelectorView, defStyleAttr, 0
        )
        val format = array.getString(R.styleable.DaySelectorView_dateFormat)
        dateFormat = SimpleDateFormat(format ?: "")
        dayViewStyle = array.getResourceId(
            R.styleable.DaySelectorView_dayViewStyle, R.style.DayViewStyle
        )
        daysCount = array.getInt(R.styleable.DaySelectorView_daysCount, daysCount)
        array.recycle()
    }

    private fun initLayout() {
        removeAllViews()
        for (index in 0 until daysCount) {
            val view = CheckedTextView(ContextThemeWrapper(context, dayViewStyle)).apply {
                layoutParams = LayoutParams(0, MATCH_PARENT, 1f)
                val day = firstDay.addDays(index)
                text = dateFormat.format(day)
                isChecked = day.isToday()
                isSelected = index == selectedIndex
                setOnClickListener {
                    onDayClickListener(day)
                }
            }
            addView(view, index)
        }
    }
}
