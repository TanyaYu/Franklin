package com.example.tanyayuferova.franklin.jobs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.tanyayuferova.franklin.utils.PreferencesUtils;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tanya Yuferova on 11/3/2017.
 */

public class EveryDayReminderUtils {

    private static final int SYNC_FLEXTIME_SECONDS = 30;
    private static final String EVERY_DAY_REMINDER_JOB_TAG = "reminder_job_tag";
    private static final String START_REMINDER_JOB_TAG = "start_job_tag";

    /**
     * Creates and schedule start job for every day reminder
     * @param context
     */
    synchronized public static void scheduleStartReminderJob(@NonNull final Context context) {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        int startSeconds = getEveryDayReminderStartSeconds(context);
        Job constraintReminderJob = dispatcher.newJobBuilder()
                .setService(EveryDayReminderFirebaseJobService.class)
                .setTag(START_REMINDER_JOB_TAG)
                .setTrigger(Trigger.executionWindow(startSeconds, startSeconds + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(constraintReminderJob);
    }
    /**
     * Creates and schedule every day reminder job
     * @param context
     */
    synchronized public static void scheduleEveryDayReminderJob(@NonNull final Context context) {

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        int startSeconds = (int) TimeUnit.HOURS.toSeconds(24);
        Job constraintReminderJob = dispatcher.newJobBuilder()
                .setService(EveryDayReminderFirebaseJobService.class)
                .setTag(EVERY_DAY_REMINDER_JOB_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(startSeconds, startSeconds + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(constraintReminderJob);

    }

    /**
     * Cancels every day reminder jobs
     * @param context
     */
    synchronized public static void cancelEveryDayReminder(@NonNull final Context context) {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        dispatcher.cancel(EVERY_DAY_REMINDER_JOB_TAG);
        dispatcher.cancel(START_REMINDER_JOB_TAG);
    }

    /**
     * Counts seconds from now to time when job should be executed
     * @param context
     * @return
     */
    private static int getEveryDayReminderStartSeconds(Context context) {
        int startMinutes = PreferencesUtils.getNotificationTime(context);
        Calendar now = Calendar.getInstance();
        Calendar reminderTime = Calendar.getInstance();
        reminderTime.set(Calendar.HOUR_OF_DAY, startMinutes / 60);
        reminderTime.set(Calendar.MINUTE, startMinutes % 60);
        reminderTime.set(Calendar.SECOND, 0);
        if(reminderTime.before(now)){
            reminderTime.add(Calendar.DAY_OF_MONTH, 1);
        }
        return (int) ((reminderTime.getTimeInMillis() - now.getTimeInMillis()) / TimeUnit.SECONDS.toMillis(1));
    }
}
