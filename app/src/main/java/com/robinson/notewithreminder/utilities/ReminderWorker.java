package com.robinson.notewithreminder.utilities;

import android.content.Context;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ReminderWorker extends Worker {
    private Context context;

    public ReminderWorker(Context context, WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    @Override
    public Result doWork() {
        long noteId = getInputData().getLong(CommonParameters.noteId, 0);
        String noteTitle = getInputData().getString(CommonParameters.noteTitle);
        String noteDescription = getInputData().getString(CommonParameters.noteDescription);
        String noteDate = getInputData().getString(CommonParameters.noteDate);
        String noteTime = getInputData().getString(CommonParameters.noteTime);

        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.createNotification(
                noteId,
                noteTitle != null ? noteTitle : "",
                noteDescription != null ? noteDescription : "",
                noteDate != null ? noteDate : "",
                noteTime != null ? noteTime : ""
        );

        MusicControl.getInstance(context).playMusic();

        return Result.success();
    }
}
