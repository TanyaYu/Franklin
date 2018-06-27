package com.tanyayuferova.franklin.data.service

import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

/**
 * Author: Tanya Yuferova
 * Date: 6/26/2018
 */
class AnalyticsService @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) {
    fun logSettingsEvent() = firebaseAnalytics.logEvent(SETTINGS_EVENT_KEY, null)

    fun logResultsEvent() = firebaseAnalytics.logEvent(RESULTS_EVENT_KEY, null)

    fun logMarkAddedEvent() = firebaseAnalytics.logEvent(MARK_ADDED_EVENT_KEY, null)

    fun logMarkRemovedEvent() = firebaseAnalytics.logEvent(MARK_REMOVED_EVENT_KEY, null)

    fun logVirtueSelectedEvent() = firebaseAnalytics.logEvent(VIRTUE_SELECTED_EVENT_KEY, null)

    fun logDaySelectedEvent() = firebaseAnalytics.logEvent(DAY_SELECTED_EVENT_KEY, null)

    companion object {
        private const val SETTINGS_EVENT_KEY = "settings"
        private const val RESULTS_EVENT_KEY = "results"
        private const val MARK_ADDED_EVENT_KEY = "mark_added"
        private const val MARK_REMOVED_EVENT_KEY = "mark_deleted"
        private const val VIRTUE_SELECTED_EVENT_KEY = "virtue_selected"
        private const val DAY_SELECTED_EVENT_KEY = "day_selected"
    }
}