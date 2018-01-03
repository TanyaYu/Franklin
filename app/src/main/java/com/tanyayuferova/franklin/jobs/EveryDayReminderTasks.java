package com.tanyayuferova.franklin.jobs;

import android.content.Context;

import com.tanyayuferova.franklin.utils.NotificationUtils;

/**
 * Created by Tanya Yuferova on 11/3/2017.
 */

public class EveryDayReminderTasks {

    static final String ACTION_EVERY_DAY_REMINDER = "every_day_reminder";

    public static void executeTask(Context context, String action) {
        if (ACTION_EVERY_DAY_REMINDER.equals(action)) {
            NotificationUtils.remindUserToEnterData(context);
        }
    }

}
