package com.tanyayuferova.franklin.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.tanyayuferova.franklin.R;

/**
 * Created by Tanya Yuferova on 11/1/2017.
 */

public class PreferencesUtils {

    /**
     * Gets notification enabled preference value
     * @param context
     * @return
     */
    public static boolean getNotificationEnabled(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(context.getString(R.string.pref_notifications_key),
                context.getResources().getBoolean(R.bool.pref_notifications_default));
    }

    /**
     * Gets notification preference time value
     * @param context
     * @return
     */
    public static int getNotificationTime(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(context.getString(R.string.pref_time_key),
                context.getResources().getInteger(R.integer.pref_notification_time));
    }

    /**
     * Gets if info has been shown
     * @param context
     * @return
     */
    public static boolean getHasInfoBeenShown(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(context.getString(R.string.pref_info_shown_key),
                context.getResources().getBoolean(R.bool.pref_info_shown_default));
    }

    /**
     * Sets if info has been shown
     * @param context
     * @param value
     */
    public static void setHasInfoBeenShown(Context context, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(context.getString(R.string.pref_info_shown_key), value);
        editor.apply();
    }
}
