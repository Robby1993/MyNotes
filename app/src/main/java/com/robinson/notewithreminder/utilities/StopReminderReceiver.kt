package com.robinson.notewithreminder.utilities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import java.util.Objects

class StopReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Objects.requireNonNull(intent.action)
            == "StopReminderReceiver"
        ) {
            MusicControl.getInstance(context)!!.stopMusic()
            val id = intent.getLongExtra(CommonParameters.noteId, 0)
            NotificationManagerCompat.from(context).cancel(id.toInt())
        }
    }
}