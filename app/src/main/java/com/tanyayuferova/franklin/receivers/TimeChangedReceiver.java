package com.tanyayuferova.franklin.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tanyayuferova.franklin.services.TimeChangeIntentService;

/**
 * Created by Tanya Yuferova on 1/4/2018.
 */

/**
 * Receives time and time zone device change
 */
public class TimeChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_TIME_CHANGED.equals(intent.getAction()) ||
                Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction())) {
            context.startService(new Intent(context, TimeChangeIntentService.class));
        }
    }
}
