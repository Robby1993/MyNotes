package com.robinson.notewithreminder.screen

import android.app.Activity
import android.app.AlarmManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.robinson.notewithreminder.R
import com.robinson.notewithreminder.database.DBManager
import com.robinson.notewithreminder.databinding.ActivityNoteAddUpdateBinding
import com.robinson.notewithreminder.fragments.InfoDialogFragment
import com.robinson.notewithreminder.utilities.CommonParameters
import com.robinson.notewithreminder.utilities.MusicControl
import com.robinson.notewithreminder.utilities.ReminderWorker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class NoteUpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteAddUpdateBinding
    private lateinit var dbManager: DBManager

    private var isUpdate = 0
    var title: String? = null
    private var desc: String? = null
    private var notificationId: Long = 0
    private var receivedDate: String? = null
    private var receivedTime: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbManager = DBManager(this)
        binding.tvTime.visibility = View.GONE
        binding.tvDate.visibility = View.GONE

        val yo1 = "ignore"
        binding.tvTime.text = yo1
        binding.tvDate.text = yo1
        getIntentData()

        binding.btnCancel.setOnClickListener { finish() }
        binding.btnAdd.setOnClickListener {
            if (isUpdate != 1) {
                // For adding to database
                addReminderInDatabase()
            } else {
                // For updating in database
                addReminderInDatabase(true)
            }
        }
    }

    private fun getIntentData() {
        if (intent != null && intent.extras != null) {
            MusicControl.getInstance(this)?.stopMusic()
            notificationId = intent.getLongExtra(CommonParameters.noteId, 0)
            title = intent.getStringExtra(CommonParameters.noteTitle)
            desc = intent.getStringExtra(CommonParameters.noteDescription)
            receivedTime = intent.getStringExtra(CommonParameters.noteTime)
            receivedDate = intent.getStringExtra(CommonParameters.noteDate)
            isUpdate = intent.getIntExtra(CommonParameters.isUpdate, 0)

            if (isUpdate == 1) {
                binding.etTitle.setText(title)
                binding.etDesc.setText(desc)
            }
            if (!receivedTime.equals("notset", ignoreCase = true)) {
                binding.tvTime.visibility = View.VISIBLE
                binding.tvDate.visibility = View.VISIBLE
                binding.tvTime.text = receivedTime
                binding.tvDate.text = receivedDate
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu1, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_reminder) {
            val fm = supportFragmentManager
            val popInfo = InfoDialogFragment()
            val bundle1 = Bundle()
            bundle1.putString(CommonParameters.noteTime, binding.tvTime.text.toString())
            bundle1.putString(CommonParameters.noteDate, binding.tvDate.text.toString())
            popInfo.arguments = bundle1
            popInfo.show(fm, "Show Fragment")
        } else if (id == R.id.menu_delete) {
            if (isUpdate == 1) {
                val info1 = AlertDialog.Builder(this)
                info1.setMessage("Are you sure you want to delete this note?")
                    .setTitle("Warning")
                    .setNeutralButton("YES") { dialog, which -> deleteMyNote(notificationId.toString()) }
                    .setPositiveButton("CANCEL") { dialog, which -> }
                    .show()
            } else {
                Toast.makeText(applicationContext, "Note is not added yet!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setReminder(
        TimeAlarm: String,
        DateAlarm: String,
        Title_Received: String?,
        Desc_Received: String?
    ) {
        val time_arr = TimeAlarm.split(":".toRegex(), limit = 2).toTypedArray()
        val date_arr = DateAlarm.split("-".toRegex(), limit = 3).toTypedArray()
        for (a in time_arr) println("Setting Holathis1 time-----$a")
        for (b in date_arr) println("Setting Holathis2 date-----$b")


        //  Time array
        val chosenHour = time_arr[0].toInt()
        val chosenMin = time_arr[1].toInt()

        //  Date array
        val chosenDay = date_arr[0].toInt()
        val chosenMonth = date_arr[1].toInt() - 1
        val chosenYear = date_arr[2].toInt()
        println("Setting alarm for " + notificationId + "at    " + chosenHour + ":" + chosenMin + " and  " + chosenDay + "-" + chosenMonth + "-" + chosenYear)
        println(
            "Setting alarm for " + "at    " + chosenHour + ":" + chosenMin + " and  " + chosenDay + "-" +
                    chosenMonth + "-" + chosenYear
        )
        // 6 Get the DateTime the user selected
        val userSelectedDateTime = Calendar.getInstance()
        userSelectedDateTime[chosenYear, chosenMonth, chosenDay, chosenHour] = chosenMin
        println("Setting alarm for getTimeInMillis" + userSelectedDateTime.timeInMillis)

        // 7 Next get DateTime for today
        val todayDateTime = Calendar.getInstance()

        // 8
        val delayInSeconds =
            userSelectedDateTime.timeInMillis / 1000L - todayDateTime.timeInMillis / 1000L
        println("Setting alarm userSelectedDateTime-------" + userSelectedDateTime.timeInMillis)
        println("Setting alarm delayInSeconds-------$delayInSeconds")
        // 9
        createWorkRequest(
            this,
            notificationId,
            Title_Received,
            Desc_Received,
            TimeAlarm,
            DateAlarm,
            delayInSeconds
        )
    }

    private fun createWorkRequest(
        context: Context?,
        id: Long,
        title: String?,
        message: String?,
        time: String?,
        date: String?,
        timeDelayInSeconds: Long
    ) {
        val myWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequest.Builder(ReminderWorker::class.java)
                .setInitialDelay(timeDelayInSeconds, TimeUnit.SECONDS)
                .setInputData(
                    Data.Builder()
                        .putLong(CommonParameters.noteId, id)
                        .putString(CommonParameters.noteTitle, title)
                        .putString(CommonParameters.noteDescription, message)
                        .putString(CommonParameters.noteDate, date)
                        .putString(CommonParameters.noteTime, time)
                        .build()
                )
                .build()
        WorkManager.getInstance(context!!).enqueue(myWorkRequest)
    }

    private fun cancelAlarm() {
        val am = getSystemService(ALARM_SERVICE) as AlarmManager
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

    private fun deleteMyNote(id: String?) {
        val selectionArgs = arrayOf(id)
        val count = dbManager.deleteReminder("ID=?", selectionArgs)
        if (count > 0) {
            callBackMainScreen()
            cancelAlarm()
            Toast.makeText(applicationContext, "Note deleted!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "Can't delete!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun addReminderInDatabase(isUpdate: Boolean = false) {
        val title = binding.etTitle.text.toString()
        val desc = binding.etDesc.text.toString()
        val time = binding.tvTime.text.toString()
        val date = binding.tvDate.text.toString()

        if (title != "" && desc != "") {
            val values = ContentValues()
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val currentDateAndTime = sdf.format(Date())
            values.put(DBManager.ColDateTime, currentDateAndTime)
            values.put(DBManager.ColTitle, title)
            values.put(DBManager.ColDescription, desc)
            if (!time.equals("ignore", ignoreCase = true)) {
                values.put(DBManager.ColRemTime, time)
                values.put(DBManager.ColRemDate, date)
                setReminder(
                    time,
                    date,
                    title,
                    desc
                )
            } else {
                values.put(DBManager.ColRemTime, "notset")
                values.put(DBManager.ColRemDate, "notset")
                // Then also cancel the alarm
            }
            if (isUpdate) {
                values.put(DBManager.ColID, notificationId)
                val selectionArgs = arrayOf<String?>(notificationId.toString())
                val count2 = dbManager.updateReminder(values, "ID=?", selectionArgs)
                if (count2 > 0) {
                    Toast.makeText(this, "Note updated!", Toast.LENGTH_SHORT).show()
                    callBackMainScreen()
                } else Toast.makeText(
                    applicationContext,
                    "Choose one Note to update!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val id = dbManager.insertReminder(values)
                if (id > 0) {
                    Toast.makeText(applicationContext, "Note Taken!", Toast.LENGTH_SHORT).show()
                    callBackMainScreen()
                } else Toast.makeText(applicationContext, "Failed to take Note", Toast.LENGTH_SHORT)
                    .show()
            }

        } else {
            Toast.makeText(
                applicationContext,
                "One or more fields are empty",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    fun setDateTime(time: String?, date: String?) {
        // Will be called from save button click of Dialog Frag
        binding.tvTime.text = time
        binding.tvDate.text = date
        binding.tvTime.visibility = View.VISIBLE
        binding.tvDate.visibility = View.VISIBLE
    }

    fun deleteRem() {
        binding.tvTime.visibility = View.GONE
        binding.tvDate.visibility = View.GONE
        val yo = "ignore"
        binding.tvTime.text = yo
        binding.tvDate.text = yo
    }

    fun callBackMainScreen() {
        val returnIntent = Intent()
        returnIntent.putExtra("resultKey", "dataToReturn")
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

}