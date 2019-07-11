package com.tanyayuferova.franklin.domain.settings

import androidx.lifecycle.ViewModel
import com.tanyayuferova.franklin.data.settings.SettingsRepository
import java.util.Date

/**
 * Author: Tanya Yuferova
 * Date: 7/8/19
 */
class SettingsViewModel(
    private val settingsRepository: SettingsRepository
): ViewModel() {

    val notificationsFlag = settingsRepository.getNotificationsFlag()
    val notificationsTime = settingsRepository.getNotificationsTime()

    fun ontNotificationFlagChanged(flag: Boolean) = settingsRepository.setNotificationFlag(flag)
    fun onNotificationTimeChanged(time: Date) = settingsRepository.setNotificationTime(time)
}