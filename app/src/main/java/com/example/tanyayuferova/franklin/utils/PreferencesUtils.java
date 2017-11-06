package com.example.tanyayuferova.franklin.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.tanyayuferova.franklin.R;

/**
 * Created by Tanya Yuferova on 11/1/2017.
 */

public class PreferencesUtils {

    /**
     * Get notification enabled preference value
     * @param context
     * @return
     */
    public static boolean getNotificationEnabled(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(context.getString(R.string.pref_notifications_key),
                context.getResources().getBoolean(R.bool.pref_notifications_default));
    }

    /**
     * Get notification preference time value
     * @param context
     * @return
     */
    public static int getNotificationTime(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(context.getString(R.string.pref_time_key),
                context.getResources().getInteger(R.integer.pref_notification_time));
    }

}
