package com.robinson.notewithreminder.utilities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.robinson.notewithreminder.R
import com.robinson.notewithreminder.screen.NoteUpdateActivity
import java.util.concurrent.atomic.AtomicInteger

open class NotificationHelper(private val context: Context) {
    private val CHANNEL_ID = "myNoteChannel"
    private var NOTIFICATION_ID: Long = 1
    fun createNotification(
        id: Long,
        title: String?,
        message: String?,
        time: String?,
        date: String?
    ) {
        // NOTIFICATION_ID = notificationID();
        NOTIFICATION_ID = id
        println("Setting createNotification NOTIFICATION_ID-------$id")
        createNotificationChannel()
        val intent = Intent(context, NoteUpdateActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(CommonParameters.noteId, NOTIFICATION_ID)
        intent.putExtra(CommonParameters.noteTitle, title)
        intent.putExtra(CommonParameters.noteDescription, message)
        intent.putExtra(CommonParameters.isUpdate, 1)
        intent.putExtra(CommonParameters.noteTime, time)
        intent.putExtra(CommonParameters.noteDate, date)
        val snoozeButton = Intent(context, StopReminderReceiver::class.java)
        snoozeButton.putExtra(CommonParameters.noteId, NOTIFICATION_ID)
        //   snoozeButton.setAction("com.robinson.notewithreminder.utilities.StopReminderReceiver");
        snoozeButton.action = "StopReminderReceiver"
        snoozeButton.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = getPendingIntent(intent)
        val action = NotificationCompat.Action.Builder(
            R.drawable.ic_notification,
            "Stop", getBroadcastPendingIntent(snoozeButton)
        ).build()
        val icon = BitmapFactory.decodeResource(context.resources, R.drawable.ic_checklist)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setLargeIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
           // .setStyle(NotificationCompat.BigPictureStyle().bigPicture(icon))
            .setContentIntent(pendingIntent)
            .addAction(action)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setLights(Color.RED, 3000, 3000)
            .setVibrate(longArrayOf(0, 1000, 1000, 1000, 1000))
            .setAutoCancel(true)
            .setGroup("GROUP_KEY")
            .setGroupSummary(true)
            .build()

        /*  if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            // Handle the case where the user doesn't grant the required permission.
            return;
        }*/

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID.toInt(), notification)
    }

    protected fun getPendingIntent(notificationIntent: Intent?): PendingIntent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    protected fun getBroadcastPendingIntent(snoozeButton: Intent?): PendingIntent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                context, NOTIFICATION_ID.toInt(), snoozeButton!!,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                context, NOTIFICATION_ID.toInt(), snoozeButton!!,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_ID,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_ID
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun notificationID(): Int {
        val c = AtomicInteger(0)
        return c.incrementAndGet()
    }
}