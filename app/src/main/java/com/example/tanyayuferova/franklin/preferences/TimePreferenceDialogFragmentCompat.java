package com.example.tanyayuferova.franklin.preferences;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TimePicker;

import com.example.tanyayuferova.franklin.R;

/**
 * The Dialog for the {@link TimePreference}.
 *
 * Created by Jakob Ulbrich at https://github.com/jakobulbrich/preferences-demo/blob/master/app/src/main/java/de/jakobulbrich/preferences/TimePreferenceDialogFragmentCompat.java
 */

public class TimePreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {

    /**
     * The TimePicker widget
     */
    private TimePicker mTimePicker;

    /**
     * Creates a new Instance of the TimePreferenceDialogFragment and stores the key of the
     * related Preference
     *
     * @param key The key of the related Preference
     * @return A new Instance of the TimePreferenceDialogFragment
     */
    public static TimePreferenceDialogFragmentCompat newInstance(String key) {
        final TimePreferenceDialogFragmentCompat
                fragment = new TimePreferenceDialogFragmentCompat();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);

        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        mTimePicker = (TimePicker) view.findViewById(R.id.time_picker);

        // Exception: There is no TimePicker with the id 'edit' in the dialog.
        if (mTimePicker == null) {
            throw new IllegalStateException("Dialog view must contain a TimePicker with id 'edit'");
        }

        // Get the time from the related Preference
        Integer minutesAfterMidnight = null;
        DialogPreference preference = getPreference();
        if (preference instanceof TimePreference) {
            minutesAfterMidnight = ((TimePreference) preference).getTime();
        }

        // Set the time to the TimePicker
        if (minutesAfterMidnight != null) {
            int hours = minutesAfterMidnight / 60;
            int minutes = minutesAfterMidnight % 60;
            boolean is24hour = DateFormat.is24HourFormat(getContext());

            mTimePicker.setIs24HourView(is24hour);
            mTimePicker.setCurrentHour(hours);
            mTimePicker.setCurrentMinute(minutes);
        }
    }

    /**
     * Called when the Dialog is closed.
     *
     * @param positiveResult Whether the Dialog was accepted or canceled.
     */
    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            // Get the current values from the TimePicker
            int hours;
            int minutes;
            if (Build.VERSION.SDK_INT >= 23) {
                hours = mTimePicker.getHour();
                minutes = mTimePicker.getMinute();
            } else {
                hours = mTimePicker.getCurrentHour();
                minutes = mTimePicker.getCurrentMinute();
            }

            // Generate value to save
            int minutesAfterMidnight = (hours * 60) + minutes;

            // Save the value
            DialogPreference preference = getPreference();
            if (preference instanceof TimePreference) {
                TimePreference timePreference = ((TimePreference) preference);
                // This allows the client to ignore the user value.
                if (timePreference.callChangeListener(minutesAfterMidnight)) {
                    // Save the value
                    timePreference.setTime(minutesAfterMidnight);
                }
            }
        }
    }
}
