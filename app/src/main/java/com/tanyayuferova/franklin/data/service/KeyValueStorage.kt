package com.tanyayuferova.franklin.data.service

import android.content.SharedPreferences

/**
 * Author: Tanya Yuferova
 * Date: 6/26/2018
 */
class KeyValueStorage(
    private val sharedPreferences: SharedPreferences
) {
    fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun putString(key: String, value: String) {
        sharedPreferences.edit()
            .putString(key, value)
            .apply()
    }

    fun getInt(key: String): Int? {
        return if(sharedPreferences.contains(key)){
            sharedPreferences.getInt(key, Int.MIN_VALUE).takeIf { it != Int.MIN_VALUE }
        }
        else null
    }

    fun putInt(key: String, value: Int) {
        sharedPreferences.edit()
            .putInt(key, value)
            .apply()
    }

    fun getBoolean(key: String): Boolean? {
        return if(sharedPreferences.contains(key)){
            sharedPreferences.getBoolean(key, false)
        }
        else null
    }

    fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit()
            .putBoolean(key, value)
            .apply()
    }

    fun remove(key: String) {
        sharedPreferences.edit()
            .remove(key)
            .apply()
    }
}
