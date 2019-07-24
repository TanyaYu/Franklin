package com.tanyayuferova.franklin.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat

/**
 * Author: Tanya Yuferova
 * Date: 7/24/19
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Context.getDimen(@DimenRes resId: Int) = resources.getDimensionPixelSize(resId)

@Suppress("NOTHING_TO_INLINE")
inline fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

@Suppress("NOTHING_TO_INLINE")
inline fun Context.getDrawableCompat(res: Int): Drawable? {
    return ContextCompat.getDrawable(this, res)
}