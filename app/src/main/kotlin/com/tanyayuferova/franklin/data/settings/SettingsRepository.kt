package com.tanyayuferova.franklin.data.settings

import androidx.lifecycle.LiveData
import com.tanyayuferova.franklin.data.sharedpreferences.KeyValueStorage

/**
 * Author: Tanya Yuferova
 * Date: 7/8/19
 */
class SettingsRepository private constructor(
    private val keyValueStorage: KeyValueStorage
) {

    fun getNotificationsFlag(): LiveData<Boolean> {
        return keyValueStorage.observe(NOTIFICATIONS_FLAG_KEY, NOTIFICATIONS_FLAG_DEFAULT)
    }

    fun getNotificationsTime(): LiveData<Int> {
        return keyValueStorage.observe(NOTIFICATION_TIME_MINUTES_KEY, NOTIFICATION_TIME_MINUTES_DEFAULT)
    }

    fun setNotificationFlag(flag: Boolean) {
        keyValueStorage.putBoolean(NOTIFICATIONS_FLAG_KEY, flag)
    }

    fun setNotificationTime(minutes: Int) {
        keyValueStorage.putInt(NOTIFICATION_TIME_MINUTES_KEY, minutes)
    }

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: SettingsRepository? = null

        fun getInstance(keyValueStorage: KeyValueStorage) =
            instance ?: synchronized(this) {
                instance
                    ?: SettingsRepository(keyValueStorage).also { instance = it }
            }

        const val NOTIFICATIONS_FLAG_KEY = "NOTIFICATIONS_FLAG_KEY_SETTING"
        const val NOTIFICATION_TIME_MINUTES_KEY = "NOTIFICATION_TIME_MINUTES_KEY_SETTING"
        const val NOTIFICATIONS_FLAG_DEFAULT = true
        const val NOTIFICATION_TIME_MINUTES_DEFAULT = 20 * 60
    }
}