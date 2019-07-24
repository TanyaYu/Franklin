package com.tanyayuferova.franklin.data

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import java.lang.RuntimeException

/**
 * Author: Tanya Yuferova
 * Date: 7/12/19
 */
class ResourceManager(
    private val context: Context
) {
    fun getString(@StringRes id: Int) = context.getString(id)

    fun getString(@StringRes id: Int, vararg formatArgs: Any): String {
        return context.getString(id, *formatArgs)
    }

    fun getStringArray(id: Int): List<String> {
        return context.resources.getStringArray(id).toList()
    }

    fun getIntArray(id: Int): List<Int> {
        return context.resources.getIntArray(id).toList()
    }

    fun getDrawable(@DrawableRes id: Int): Drawable {
        return ContextCompat.getDrawable(context, id) ?:
                throw RuntimeException("Resource $id not found")
    }

    companion object {
        @Volatile
        private var instance: ResourceManager? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
                instance
                    ?: ResourceManager(context).also { instance = it }
            }

    }
}