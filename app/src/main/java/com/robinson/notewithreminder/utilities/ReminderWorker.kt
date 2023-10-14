package com.robinson.notewithreminder.utilities

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.robinson.notewithreminder.utilities.MusicControl.Companion.getInstance

class ReminderWorker(private val context: Context, params: WorkerParameters?) : Worker(
    context, params!!
) {
    override fun doWork(): Result {
        val noteId = inputData.getLong(CommonParameters.noteId, 0)
        val noteTitle = inputData.getString(CommonParameters.noteTitle)
        val noteDescription = inputData.getString(CommonParameters.noteDescription)
        val noteDate = inputData.getString(CommonParameters.noteDate)
        val noteTime = inputData.getString(CommonParameters.noteTime)
        val notificationHelper = NotificationHelper(context)
        notificationHelper.createNotification(
            noteId,
            noteTitle ?: "",
            noteDescription ?: "",
            noteDate ?: "",
            noteTime ?: ""
        )
        getInstance(context)!!.playMusic()

        return Result.success()
    }
}