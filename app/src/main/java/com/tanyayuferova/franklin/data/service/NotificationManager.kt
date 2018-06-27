package com.tanyayuferova.franklin.data.service

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import com.tanyayuferova.franklin.R
import com.tanyayuferova.franklin.ui.common.MainActivity
import javax.inject.Inject

/**
 * Author: Tanya Yuferova
 * Date: 6/26/2018
 */
class NotificationManager @Inject constructor(
    private val context: Context,
    private val systemNotificationManager: NotificationManager
) {

    fun notifyUserToMakeMarks() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createReminderChannel()
        }

        val notificationBuilder = NotificationCompat.Builder(
            context,
            NOTIFICATION_REMINDERS_CHANEL_ID
        )
            .setColor(ContextCompat.getColor(context, R.color.primary_dark))
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setLargeIcon(largeIcon(context, R.drawable.ic_notification_icon))
            .setContentTitle(context.getString(R.string.reminder_notification_title))
            .setContentText(context.getString(R.string.reminder_notification_body))
            .setStyle(NotificationCompat.BigTextStyle().bigText(
                context.getString(R.string.reminder_notification_body)))
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setContentIntent(contentIntent())
//            .setPriority(Notification.PRIORITY_HIGH)
            .setAutoCancel(true)

        systemNotificationManager.notify(REMINDER_NOTIFICATION_ID, notificationBuilder.build())
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createReminderChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_REMINDERS_CHANEL_ID,
            context.getString(R.string.notification_reminder_channel_title),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            enableLights(false)
            enableVibration(true)
            setShowBadge(true)
        }
        systemNotificationManager.createNotificationChannel(channel)
    }

    private fun contentIntent(): PendingIntent {
        //todo test me navigation
        val startActivityIntent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(
            context,
            REMINDER_PENDING_INTENT_ID,
            startActivityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun largeIcon(context: Context, resource: Int): Bitmap {
        return BitmapFactory.decodeResource(context.resources, resource)
    }

    companion object {
        private const val NOTIFICATION_REMINDERS_CHANEL_ID = "reminder_channel"
        private const val REMINDER_NOTIFICATION_ID = 123;
        private const val REMINDER_PENDING_INTENT_ID = 456;
    }
}