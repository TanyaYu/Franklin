package com.tanyayuferova.franklin.domain.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tanyayuferova.franklin.data.settings.SettingsRepository

/**
 * Author: Tanya Yuferova
 * Date: 7/8/19
 */
class SettingsViewModelFactory(
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  SettingsViewModel(
            settingsRepository
        ) as T
    }
}