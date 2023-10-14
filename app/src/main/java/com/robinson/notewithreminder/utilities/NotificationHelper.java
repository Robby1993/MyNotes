package com.robinson.notewithreminder.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.robinson.notewithreminder.R;
import com.robinson.notewithreminder.screen.NoteUpdateActivity;

import java.util.concurrent.atomic.AtomicInteger;

public class NotificationHelper {
    private final Context context;
    private final String CHANNEL_ID = "myNoteChannel";
    private long NOTIFICATION_ID = 1;

    public NotificationHelper(Context context) {
        this.context = context;
    }

    public void createNotification(Long id, String title, String message, String time, String date) {
       // NOTIFICATION_ID = notificationID();
        NOTIFICATION_ID = id;

        System.out.println("Setting createNotification NOTIFICATION_ID-------" + id);

        createNotificationChannel();

        Intent intent = new Intent(context, NoteUpdateActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(CommonParameters.noteId, NOTIFICATION_ID);
        intent.putExtra(CommonParameters.noteTitle, title);
        intent.putExtra(CommonParameters.noteDescription, message);
        intent.putExtra(CommonParameters.isUpdate, 1);
        intent.putExtra(CommonParameters.noteTime, time);
        intent.putExtra(CommonParameters.noteDate, date);

        Intent snoozeButton = new Intent(context, StopReminderReceiver.class);
        snoozeButton.putExtra(CommonParameters.noteId, NOTIFICATION_ID);
     //   snoozeButton.setAction("com.robinson.notewithreminder.utilities.StopReminderReceiver");
        snoozeButton.setAction("StopReminderReceiver");
        snoozeButton.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = getPendingIntent(intent);

        NotificationCompat.Action action = new NotificationCompat.Action.Builder(
                R.drawable.ic_notification,
                "Stop", getBroadcastPendingIntent(snoozeButton)
        ).build();

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_checklist);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(icon)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(icon))
                .setContentIntent(pendingIntent)
                .addAction(action)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLights(Color.RED, 3000, 3000)
                .setVibrate(new long[]{0, 1000, 1000, 1000, 1000})
                .setAutoCancel(true)
                .setGroup("GROUP_KEY")
                .setGroupSummary(true)
                .build();

      /*  if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            // Handle the case where the user doesn't grant the required permission.
            return;
        }*/

        NotificationManagerCompat.from(context).notify((int) NOTIFICATION_ID, notification);
    }

    protected PendingIntent getPendingIntent(Intent notificationIntent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return PendingIntent.getActivity(context, 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            return PendingIntent.getActivity(context, 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    protected PendingIntent getBroadcastPendingIntent(Intent snoozeButton) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return PendingIntent.getBroadcast(context, (int) NOTIFICATION_ID, snoozeButton,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        } else {
            return PendingIntent.getBroadcast(context, (int) NOTIFICATION_ID, snoozeButton,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_ID);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private int notificationID() {
        AtomicInteger c = new AtomicInteger(0);
        return c.incrementAndGet();
    }
}
