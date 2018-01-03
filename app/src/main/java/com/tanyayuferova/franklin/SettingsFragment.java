package com.tanyayuferova.franklin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import com.tanyayuferova.franklin.jobs.EveryDayReminderUtils;
import com.tanyayuferova.franklin.preferences.TimePreference;
import com.tanyayuferova.franklin.preferences.TimePreferenceDialogFragmentCompat;
import com.tanyayuferova.franklin.utils.PreferencesUtils;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tanya Yuferova on 11/6/2017.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.pref_screen);

        PreferenceScreen prefScreen = getPreferenceScreen();
        int count = prefScreen.getPreferenceCount();

       //  Go through all of the preferences, and set up their preference summary.
        for (int i = 0; i < count; i++) {
            Preference p = prefScreen.getPreference(i);
            // We need to set up preference summaries for TimePreference
            if (p instanceof TimePreference) {
                setTimePreferenceSummary((TimePreference) p);
            }
        }

        /* Listener to hide notification time preference */
        Preference preference = findPreference(getString(R.string.pref_notifications_key));
        preference.setOnPreferenceChangeListener(this);
        /* Set visibility of time preference */
        findPreference(getString(R.string.pref_time_key)).setVisible(PreferencesUtils.getNotificationEnabled(getContext()));
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Figure out which preference was changed
        Preference p = findPreference(key);
        if (null != p) {
            /* We need to update preference summaries for TimePreference */
            if (p instanceof TimePreference) {
                setTimePreferenceSummary((TimePreference) p);
            }

            /* We need to reschedule notifications after properties changed */
            boolean isNotificationsEnabled = PreferencesUtils.getNotificationEnabled(getContext());
            if(key.equals(getString(R.string.pref_time_key)) && isNotificationsEnabled){
                EveryDayReminderUtils.scheduleStartReminderJob(getContext());
            }
            else if(key.equals(getString(R.string.pref_notifications_key))) {
                if(isNotificationsEnabled)
                    EveryDayReminderUtils.scheduleStartReminderJob(getContext());
                else EveryDayReminderUtils.cancelEveryDayReminder(getContext());
            }
        }
    }

    /**
     * Updates the summary for the TimePreference
     *
     * @param preference The TimePreference to be updated
     */
    private void setTimePreferenceSummary(TimePreference preference) {
        Date date = new Date(TimeUnit.MINUTES.toMillis(preference.getTime()));
        DateFormat dateFormat = android.text.format.DateFormat.getTimeFormat(getContext());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        preference.setSummary(dateFormat.format(date));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if(preference.getKey().equals(getString(R.string.pref_notifications_key))){
            findPreference(getString(R.string.pref_time_key)).setVisible((boolean) newValue);
        }
        return true;
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        // Try if the preference is one of our custom Preferences
        DialogFragment dialogFragment = null;
        if (preference instanceof TimePreference) {
            // Create a new instance of TimePreferenceDialogFragment with the key of the related
            // Preference
            dialogFragment = TimePreferenceDialogFragmentCompat.newInstance(preference.getKey());
        }

        if (dialogFragment != null) {
            // The dialog was created (it was one of our custom Preferences), show the dialog for it
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(this.getFragmentManager(), "android.support.v7.preference" +
                    ".PreferenceFragment.DIALOG");
        } else {
            // Dialog creation could not be handled here. Try with the super method.
            super.onDisplayPreferenceDialog(preference);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
