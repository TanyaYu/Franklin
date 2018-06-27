package com.tanyayuferova.franklin.data.service

import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.Lifetime
import com.firebase.jobdispatcher.Trigger
import com.tanyayuferova.franklin.domain.preferences.PreferencesInteractor
import com.tanyayuferova.franklin.domain.notifications.EveryDayReminderFirebaseJobService
import com.tanyayuferova.franklin.domain.notifications.StartReminderFirebaseJobService
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Author: Tanya Yuferova
 * Date: 6/26/2018
 */
class DailyReminderManager @Inject constructor(
    private val notificationManager: NotificationManager,
    private val firebaseJobDispatcher: FirebaseJobDispatcher,
    private val preferencesInteractor: PreferencesInteractor
) {
    /**
     * Creates and schedule start job for every day reminder
     *
     * The app should notify user daily at the same time. However, when we schedule a recurring job
     * we can't set time delay to perform reminder at specific time. For that reason we use two jobs.
     * The first one is START_REMINDER_JOB_TAG. It executes first notification and creates recurring job -
     * EVERY_DAY_REMINDER_JOB_TAG, which is responsible for daily reminders.
     *
     * @param context
     */
    @Synchronized
    fun scheduleStartReminderJob() {
        val startSeconds = getEveryDayReminderStartSeconds()
        val constraintReminderJob = firebaseJobDispatcher.newJobBuilder()
            .setService(StartReminderFirebaseJobService::class.java)
            .setTag(START_REMINDER_JOB_TAG)
            .setTrigger(Trigger.executionWindow(startSeconds, startSeconds + SYNC_FLEXTIME_SECONDS))
            .setReplaceCurrent(true)
            .build()

        firebaseJobDispatcher.schedule(constraintReminderJob)

        // Cancel old reminder job
        firebaseJobDispatcher.cancel(EVERY_DAY_REMINDER_JOB_TAG)
    }

    /**
     * Creates and schedule every day reminder job
     * @param context
     */
    @Synchronized
    fun scheduleEveryDayReminderJob() {
        val startSeconds = TimeUnit.HOURS.toSeconds(24).toInt()
        val constraintReminderJob = firebaseJobDispatcher.newJobBuilder()
            .setService(EveryDayReminderFirebaseJobService::class.java)
            .setTag(EVERY_DAY_REMINDER_JOB_TAG)
            .setLifetime(Lifetime.FOREVER)
            .setRecurring(true)
            .setTrigger(Trigger.executionWindow(startSeconds, startSeconds + SYNC_FLEXTIME_SECONDS))
            .setReplaceCurrent(true)
            .build()

        firebaseJobDispatcher.schedule(constraintReminderJob)

    }

    /**
     * Cancels every day reminder jobs
     * @param context
     */
    @Synchronized
    fun cancelEveryDayReminder() {
        firebaseJobDispatcher.cancel(EVERY_DAY_REMINDER_JOB_TAG)
        firebaseJobDispatcher.cancel(START_REMINDER_JOB_TAG)
    }

    /**
     * Counts seconds from now to time when job should be executed
     * @param context
     * @return
     */
    private fun getEveryDayReminderStartSeconds(): Int {
        val startMinutes = preferencesInteractor.getNotificationTime()
        val now = Calendar.getInstance()
        val reminderTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, startMinutes / 60)
            set(Calendar.MINUTE, startMinutes % 60)
            set(Calendar.SECOND, 0)
        }
        if (reminderTime.before(now)) {
            reminderTime.add(Calendar.DAY_OF_MONTH, 1)
        }
        return ((reminderTime.timeInMillis - now.timeInMillis) / TimeUnit.SECONDS.toMillis(1)).toInt()
    }

    fun executeEveryDayReminder() {
        notificationManager.notifyUserToMakeMarks()
    }

    companion object {
        private const val SYNC_FLEXTIME_SECONDS = 30
        private const val EVERY_DAY_REMINDER_JOB_TAG = "reminder_job_tag"
        private const val START_REMINDER_JOB_TAG = "start_job_tag"
    }
}