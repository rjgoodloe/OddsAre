package com.android.oddsare.generic

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.android.oddsare.R
import com.android.oddsare.activity.MainActivity

class Notifications {

    private val notifyTag = "New Odds Request"

    fun notify(context: Context, message: String, channelID: Int) {

        val intent = Intent(context, MainActivity::class.java)

        val builder = NotificationCompat.Builder(context, channelID.toString())
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentTitle("New Odds Request")
            .setContentText(message)
            .setNumber(channelID)
            .setSmallIcon(R.drawable.ic_temp_logo)
            .setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
            .setAutoCancel(true)
            .setChannelId(channelID.toString())


        val notificationChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(channelID.toString(), "NOTIFICATION", NotificationManager.IMPORTANCE_DEFAULT)

        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)

        notificationManager.notify(notifyTag, 0, builder.build())


    }
}