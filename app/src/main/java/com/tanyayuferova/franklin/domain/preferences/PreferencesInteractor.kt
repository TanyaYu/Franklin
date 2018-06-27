package com.tanyayuferova.franklin.domain.preferences

import com.tanyayuferova.franklin.data.service.KeyValueStorage
import javax.inject.Inject

/**
 * Author: Tanya Yuferova
 * Date: 6/26/2018
 */
class PreferencesInteractor @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {
    fun getNotificationEnabled(): Boolean = keyValueStorage.getBoolean(ARE_NOTIFICATIONS_ACTIVE_KEY) ?: false

    fun setNotificationEnabled(value: Boolean) = keyValueStorage.putBoolean(ARE_NOTIFICATIONS_ACTIVE_KEY, value)

    fun getNotificationTime(): Int = keyValueStorage.getInt(NOTIFICATIONS_TIME_KEY) ?: 1200

    fun setNotificationTime(value: Int) = keyValueStorage.putInt(NOTIFICATIONS_TIME_KEY, value)

    fun getHasInfoBeenShown(): Boolean = keyValueStorage.getBoolean(WAS_INFO_SHOWN_KEY) ?: false

    fun setHasInfoBeenShown(value: Boolean) = keyValueStorage.putBoolean(WAS_INFO_SHOWN_KEY, value)

    companion object {
        private const val ARE_NOTIFICATIONS_ACTIVE_KEY = "preference_notifications"
        private const val NOTIFICATIONS_TIME_KEY = "preference_time"
        private const val WAS_INFO_SHOWN_KEY = "preference_info_shown"
    }
}