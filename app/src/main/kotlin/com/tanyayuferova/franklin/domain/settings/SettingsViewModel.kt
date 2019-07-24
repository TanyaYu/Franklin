package com.tanyayuferova.franklin.domain.settings

import androidx.lifecycle.ViewModel
import com.tanyayuferova.franklin.data.settings.SettingsRepository
import com.tanyayuferova.franklin.utils.DateFormatter
import com.tanyayuferova.franklin.utils.map
import com.tanyayuferova.franklin.utils.setUpTotalMinutes
import java.util.Date

/**
 * Author: Tanya Yuferova
 * Date: 7/8/19
 */
class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val dateFormatter: DateFormatter
): ViewModel() {

    val notificationsFlag = settingsRepository.getNotificationsFlag()

    val notificationsTime = settingsRepository
        .getNotificationsTime()
        .map { minutes ->
            minutes / 60 to minutes % 60
        }

    val notificationsTimeString = settingsRepository
        .getNotificationsTime()
        .map {  minutes ->
            val date = Date().setUpTotalMinutes(minutes)
            dateFormatter.formatTimeShort(date)
        }

    fun ontNotificationFlagChanged(flag: Boolean) = settingsRepository.setNotificationFlag(flag)

    fun onNotificationTimeChanged(hour: Int, minutes: Int) {
        settingsRepository.setNotificationTime(hour * 60 + minutes)
    }
}