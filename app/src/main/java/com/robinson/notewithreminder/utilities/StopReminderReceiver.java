package com.robinson.notewithreminder.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationManagerCompat;
import java.util.Objects;

public class StopReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.requireNonNull(intent.getAction())
                .equals("StopReminderReceiver")) {

            new MusicControl(context).stopMusic();
            long id = intent.getLongExtra(CommonParameters.noteId, 0);
            NotificationManagerCompat.from(context).cancel((int) id);
        }
    }
}
