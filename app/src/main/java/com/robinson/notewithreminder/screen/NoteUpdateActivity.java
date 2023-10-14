package com.robinson.notewithreminder.screen;

import android.app.AlarmManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.robinson.notewithreminder.fragments.InfoDialogFragment;
import com.robinson.notewithreminder.R;
import com.robinson.notewithreminder.database.DBManager;
import com.robinson.notewithreminder.utilities.CommonParameters;
import com.robinson.notewithreminder.utilities.ReminderWorker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class NoteUpdateActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "Reminder";
    DBManager dbManager;
    Button add_update1, cancel1;
    int isUpdate;
    String title_received1;
    String desc_received1;
    EditText title_value, desc_value;
    long notificationId;
    TextView RemTime, RemDate;
    String RemDate_received, RemTime_received;
    public static String time_pass, date_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add_update);
        dbManager = new DBManager(this);
        add_update1 = (Button) findViewById(R.id.add_update_button1);
        cancel1 = (Button) findViewById(R.id.cancel_button1);
        title_value = (EditText) findViewById(R.id.title_value);
        desc_value = (EditText) findViewById(R.id.desc_value);
        RemTime = (TextView) findViewById(R.id.rem_time1);
        RemDate = (TextView) findViewById(R.id.rem_date1);

        RemTime.setVisibility(View.GONE);
        RemDate.setVisibility(View.GONE);
        String yo1 = "ignore";
        RemTime.setText(yo1);
        RemDate.setText(yo1);

        getIntentData();
    }

    void getIntentData() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            notificationId = getIntent().getLongExtra(CommonParameters.noteId, 0);
            title_received1 = getIntent().getStringExtra(CommonParameters.noteTitle);
            desc_received1 = getIntent().getStringExtra(CommonParameters.noteDescription);
            RemTime_received = getIntent().getStringExtra(CommonParameters.noteTime);
            RemDate_received = getIntent().getStringExtra(CommonParameters.noteDate);
            isUpdate = getIntent().getIntExtra(CommonParameters.isUpdate, 0);
            if (isUpdate == 1) {
                title_value.setText(title_received1);
                desc_value.setText(desc_received1);
            }
            if (!RemTime_received.equalsIgnoreCase("notset")) {
                RemTime.setVisibility(View.VISIBLE);
                RemDate.setVisibility(View.VISIBLE);
                RemTime.setText(RemTime_received);
                RemDate.setText(RemDate_received);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_reminder) {
            androidx.fragment.app.FragmentManager fm = getSupportFragmentManager();
            InfoDialogFragment popInfo = new InfoDialogFragment();
            Bundle bundle1 = new Bundle();
            bundle1.putString(CommonParameters.noteTime, RemTime.getText().toString());
            bundle1.putString(CommonParameters.noteDate, RemDate.getText().toString());
            popInfo.setArguments(bundle1);

            popInfo.show(fm, "Show Fragment");
//                Toast.makeText(getApplicationContext(),"Reminder is currently under development!",Toast.LENGTH_SHORT).show();


        } else if (id == R.id.menu_delete) {
            if (isUpdate == 1) {
                AlertDialog.Builder info1 = new AlertDialog.Builder(this);
                info1.setMessage("Are you sure you want to delete this note?")
                        .setTitle("Warning")
                        .setNeutralButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delete_element(String.valueOf(notificationId));
                            }
                        })
                        .setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

            } else {
                Toast.makeText(getApplicationContext(), "Note is not added yet!", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void setReminder(String TimeAlarm, String DateAlarm, String Title_Received, String Desc_Received) {

        String[] time_arr = TimeAlarm.split(":", 2);
        String[] date_arr = DateAlarm.split("-", 3);
        for (String a : time_arr)
            System.out.println("Setting Holathis1 time-----" + a);

        for (String b : date_arr)
            System.out.println("Setting Holathis2 date-----" + b);


        //  Time array
        int chosenHour = Integer.parseInt(time_arr[0]);
        int chosenMin = Integer.parseInt(time_arr[1]);

        //  Date array
        int chosenDay = Integer.parseInt(date_arr[0]);
        int chosenMonth = Integer.parseInt(date_arr[1])-1;
        int chosenYear = Integer.parseInt(date_arr[2]);


        System.out.println("Setting alarm for " + notificationId + "at    " + chosenHour + ":" + chosenMin + " and  " + chosenDay + "-" + chosenMonth + "-" + chosenYear);

        System.out.println(
                "Setting alarm for " +  "at    " + chosenHour + ":" + chosenMin + " and  " + chosenDay + "-" +
                        chosenMonth+ "-" + chosenYear
        );
        // 6 Get the DateTime the user selected
        Calendar userSelectedDateTime = Calendar.getInstance();
        userSelectedDateTime.set(chosenYear, chosenMonth, chosenDay, chosenHour, chosenMin);
        System.out.println("Setting alarm for getTimeInMillis" + userSelectedDateTime.getTimeInMillis());

        // 7 Next get DateTime for today
        Calendar todayDateTime = Calendar.getInstance();

        // 8
        long delayInSeconds =
                (userSelectedDateTime.getTimeInMillis() / 1000L) - (todayDateTime.getTimeInMillis() / 1000L);

        System.out.println("Setting alarm userSelectedDateTime-------" + userSelectedDateTime.getTimeInMillis());
        System.out.println("Setting alarm delayInSeconds-------" + delayInSeconds);
        // 9
        createWorkRequest(this,
                notificationId,
                Title_Received,
                Desc_Received,
                TimeAlarm,
                DateAlarm,
                delayInSeconds);
    }

    public void createWorkRequest(Context context,
                                  long id,
                                  String title,
                                  String message,
                                  String time,
                                  String date,
                                  long timeDelayInSeconds) {
        OneTimeWorkRequest myWorkRequest = new OneTimeWorkRequest.Builder(ReminderWorker.class)
                .setInitialDelay(timeDelayInSeconds, TimeUnit.SECONDS)
                .setInputData(
                        new Data.Builder()
                                .putLong(CommonParameters.noteId, id)
                                .putString(CommonParameters.noteTitle, title)
                                .putString(CommonParameters.noteDescription, message)
                                .putString(CommonParameters.noteDate, date)
                                .putString(CommonParameters.noteTime, time)
                                .build()
                )
                .build();

        WorkManager.getInstance(context).enqueue(myWorkRequest);
    }

    public void CancelAlarm() {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
       /* Intent intent = new Intent(this, MyReceiver.class);
        intent.setAction("com.akashmanna.rem");
        String msg1 = "Hello from Keep Notes";
        intent.putExtra("AlarmMessage", msg1);
        intent.putExtra("NotiID", RecordID_received1);
        intent.putExtra("Noti_Title", "ignore");
        intent.putExtra("Noti_Desc", "ignore");
        intent.putExtra("Rem_Time", "ignore");
        intent.putExtra("Rem_Date", "ignore");
        intent.putExtra("SetNotify", "SetNotificationNot");
        PendingIntent pi = PendingIntent.getBroadcast(this, RecordID_received, intent, PendingIntent.FLAG_IMMUTABLE);
        am.cancel(pi);
        System.out.println("Canceling alarm for " + RecordID_received);*/


//        CancelNotification();
    }


    public void delete_element(String ID1) {
        String[] SelectionArgs = {ID1};
        int count = dbManager.deleteReminder("ID=?", SelectionArgs);
        if (count > 0) {
            finish();
            CancelAlarm();
            Toast.makeText(getApplicationContext(), "Note deleted!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Can't delete!", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveButtonAddUpdate(View view) {
        if (isUpdate != 1) {
            // For adding to database
            addReminderInDatabase();
        } else {
            // For updating in database
            update_database();
        }

    }

    public void addReminderInDatabase() {
        if (!title_value.getText().toString().equals("") && !desc_value.getText().toString().equals("")) {
            ContentValues values = new ContentValues();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDateAndTime = sdf.format(new Date());

            values.put(DBManager.ColDateTime, currentDateAndTime);
            values.put(DBManager.ColTitle, title_value.getText().toString());
            values.put(DBManager.ColDescription, desc_value.getText().toString());

            if (!RemTime.getText().toString().equalsIgnoreCase("ignore")) {
                values.put(DBManager.ColRemTime, RemTime.getText().toString());
                values.put(DBManager.ColRemDate, RemDate.getText().toString());
                setReminder(
                        RemTime.getText().toString(),
                        RemDate.getText().toString(),
                        title_value.getText().toString(),
                        desc_value.getText().toString());
            } else {
                values.put(DBManager.ColRemTime, "notset");
                values.put(DBManager.ColRemDate, "notset");
                // Then also cancel the alarm
            }
            long id = dbManager.insertReminder(values);
            if (id > 0) {
                Toast.makeText(getApplicationContext(), "Note Taken!", Toast.LENGTH_SHORT).show();
                finish();
            } else
                Toast.makeText(getApplicationContext(), "Failed to take Note", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "One or more fields are empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void update_database() {
        if (!title_value.getText().toString().equals("") && !desc_value.getText().toString().equals("")) {
            ContentValues values = new ContentValues();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            values.put(DBManager.ColDateTime, currentDateandTime);
            values.put(DBManager.ColTitle, title_value.getText().toString());
            values.put(DBManager.ColDescription, desc_value.getText().toString());
            values.put(DBManager.ColID, notificationId);

            if (!RemTime.getText().toString().equalsIgnoreCase("ignore")) {
                values.put(DBManager.ColRemTime, RemTime.getText().toString());
                values.put(DBManager.ColRemDate, RemDate.getText().toString());
                setReminder(RemTime.getText().toString(), RemDate.getText().toString(), title_value.getText().toString(), desc_value.getText().toString());

            } else {
                values.put(DBManager.ColRemTime, "notset");
                values.put(DBManager.ColRemDate, "notset");
                CancelAlarm();
            }

            String[] SelectionArgs = {String.valueOf(notificationId)};
            int count2 = dbManager.updateReminder(values, "ID=?", SelectionArgs);
            //  long id = dbManager.insertReminder(values);
            if (count2 > 0) {
                Toast.makeText(this, "Note updated!", Toast.LENGTH_SHORT).show();
                finish();
            } else
                Toast.makeText(getApplicationContext(), "Choose one Note to update!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "One or more fields are empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void close_act(View view) {
        finish();
    }


    public void setDateTime(String time, String date) {
        // Will be called from save button click of Dialog Frag
        RemTime.setText(time);
        RemDate.setText(date);
        RemTime.setVisibility(View.VISIBLE);
        RemDate.setVisibility(View.VISIBLE);
    }

    public void deleteRem() {
        RemTime.setVisibility(View.GONE);
        RemDate.setVisibility(View.GONE);
        String yo = "ignore";
        RemTime.setText(yo);
        RemDate.setText(yo);
    }

    public void setTime_pass(String t) {
        time_pass = t;
    }

    public void setDate_pass(String d) {
        date_pass = d;
    }

}
