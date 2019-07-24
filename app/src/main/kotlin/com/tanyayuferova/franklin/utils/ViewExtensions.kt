package com.tanyayuferova.franklin.utils

import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.core.widget.TextViewCompat

/**
 * Author: Tanya Yuferova
 * Date: 7/13/19
 */
fun TextView.setTextAppreance_(@StyleRes resId: Int) {
    TextViewCompat.setTextAppearance(this, resId)
}
//todo rename me