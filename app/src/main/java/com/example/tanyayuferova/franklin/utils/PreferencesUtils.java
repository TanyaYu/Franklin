package com.example.tanyayuferova.franklin.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tanyayuferova.franklin.R;

/**
 * Created by Tanya Yuferova on 11/1/2017.
 */

public class PreferencesUtils {

    /**
     * Returns current virtue id
     * @param sharedPreferences
     * @param context
     * @return
     */
    public static int getSelectedVirtueId(SharedPreferences sharedPreferences, Context context) {
        if(sharedPreferences.contains(context.getString(R.string.pref_virtue_id_key))) {
            return sharedPreferences.getInt(context.getString(R.string.pref_virtue_id_key),
                    context.getResources().getInteger(R.integer.pref_virtue_id_default));
        }
        return 0;
    }

    /**
     * Sets current virtue id
     * @param value virtue id
     * @param sharedPreferences
     * @param context
     * @return
     */
    public static boolean setSelectedVirtueId(int value, SharedPreferences sharedPreferences, Context context){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(context.getString(R.string.pref_virtue_id_key), value);
        return editor.commit();
    }
}
