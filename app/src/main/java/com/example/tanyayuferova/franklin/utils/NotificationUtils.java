package com.example.tanyayuferova.franklin.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.tanyayuferova.franklin.MainActivity;
import com.example.tanyayuferova.franklin.R;

/**
 * Created by Tanya Yuferova on 11/3/2017.
 */

public class NotificationUtils {

    private static final int REMINDER_NOTIFICATION_ID = 123;
    private static final int REMINDER_PENDING_INTENT_ID = 456;

    /**
     * Builds notification that says user to enter today's data
     * @param context
     */
    public static void remindUserToEnterData(Context context) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                .setSmallIcon(R.drawable.ic_top_hat_with_moustache)
                .setLargeIcon(largeIcon(context, R.drawable.ic_top_hat_with_moustache))
                .setContentTitle(context.getString(R.string.reminder_notification_title))
                .setContentText(context.getString(R.string.reminder_notification_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.reminder_notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(REMINDER_NOTIFICATION_ID, notificationBuilder.build());
    }

    /**
     * Content intent that opens after user tap on notification
     * @param context
     * @return
     */
    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(
                context,
                REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Bitmap largeIcon(Context context, int resource) {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, resource);
        return largeIcon;
    }
}
