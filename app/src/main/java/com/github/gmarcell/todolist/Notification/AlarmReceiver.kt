package com.github.gmarcell.todolist.notification

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.github.gmarcell.todolist.AddEditDoesActivity
import com.github.gmarcell.todolist.R


@Suppress("DEPRECATION", "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationIntent = Intent(context, NotifActivity::class.java)
        notificationIntent.putExtras(intent)

        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(NotifActivity::class.java)
        stackBuilder.addNextIntent(notificationIntent)

        val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = Notification.Builder(context)
        val notification =
            builder.setContentTitle(intent.getStringExtra(AddEditDoesActivity.EXTRA_TITLE))
                .setContentText("Do it Now MORON!!!")
                .setTicker("New Task is Due!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build()

        val a = longArrayOf(100, 1000, 1000)
        val vibrator =
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        // Vibrate for 10000 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(a, 0))
        } else { //deprecated in API 26
            vibrator.vibrate(10000)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID)
        }
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "NotificationDemo",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notification)
    }

    companion object {
        private const val CHANNEL_ID = "com.github.todolist.notification"
    }
}