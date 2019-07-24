package com.tanyayuferova.franklin.data.sharedpreferences

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tanyayuferova.franklin.utils.onActive
import com.tanyayuferova.franklin.utils.onInactive

/**
 * Author: Tanya Yuferova
 * Date: 7/7/2019
 */
class KeyValueStorage private constructor(
    private val sharedPreferences: SharedPreferences
) {

    @Suppress("UNCHECKED_CAST")
    fun <T> observe(key: String, default: T): LiveData<T> {
        lateinit var listener: PreferenceChangeListener<T>
        val liveData = MutableLiveData<T>()
            .onActive {
                sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
            }
            .onInactive {
                sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
            } as MutableLiveData<T>
        listener = PreferenceChangeListener(key) { newValue ->
            liveData.value = newValue
        }
        val currentValue = sharedPreferences.all[key] ?: default
        liveData.value = currentValue as T
        return liveData
    }

    fun putString(key: String, value: String) {
        sharedPreferences
            .edit()
            .putString(key, value)
            .apply()
    }

    fun getString(key: String): String? = sharedPreferences.getString(key, null)

    fun putLong(key: String, value: Long) = sharedPreferences
        .edit()
        .putLong(key, value)
        .apply()

    fun getLong(key: String): Long? {
        val value = sharedPreferences.getLong(key, Long.MIN_VALUE)
        return if (value == Long.MIN_VALUE) null
        else value
    }

    fun putInt(key: String, value: Int) = sharedPreferences
        .edit()
        .putInt(key, value)
        .apply()

    fun getInt(key: String): Int? {
        val value = sharedPreferences.getInt(key, Int.MIN_VALUE)
        return if (value == Int.MIN_VALUE) null
        else value
    }

    fun getInt(key: String, default: Int): Int {
        val value = sharedPreferences.getInt(key, Int.MIN_VALUE)
        return if (value == Int.MIN_VALUE) default
        else value
    }

    fun putBoolean(key: String, value: Boolean) = sharedPreferences
        .edit()
        .putBoolean(key, value)
        .apply()


    fun getBoolean(key: String, default: Boolean = false): Boolean =
        sharedPreferences.getBoolean(key, default)

    fun putFloat(key: String, value: Float) = sharedPreferences
        .edit()
        .putFloat(key, value)
        .apply()

    fun getFloat(key: String): Float? {
        val value = sharedPreferences.getFloat(key, Float.MIN_VALUE)
        return if (value == Float.MIN_VALUE) null
        else value
    }

    fun putStringSet(key: String, value: MutableSet<String>) = sharedPreferences
        .edit()
        .putStringSet(key, value)
        .apply()

    fun getStringSet(key: String): MutableSet<String>? = sharedPreferences.getStringSet(key, null)

    fun clear(keys: Array<String>?) = sharedPreferences.edit()
        .also { editor ->
            keys?.forEach {
                editor.remove(it)
            } ?: editor.clear()
        }.apply()

    companion object {
        @Volatile
        private var instance: KeyValueStorage? = null

        fun getInstance(
            sharedPreferences: SharedPreferences
        ) =
            instance ?: synchronized(this) {
                instance
                    ?: KeyValueStorage(
                        sharedPreferences
                    ).also { instance = it }
            }
    }
}


