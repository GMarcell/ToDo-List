package com.github.gmarcell.todolist

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.github.gmarcell.todolist.notification.AlarmReceiver
import kotlinx.android.synthetic.main.activity_add_does.*
import java.util.*


class AddEditDoesActivity : AppCompatActivity() {

    private val cal = Calendar.getInstance()

    companion object {
        const val EXTRA_ID = "com.github.gmarcell.todolist.EXTRA_ID"
        const val EXTRA_TITLE = "com.github.gmarcell.todolist.EXTRA_TITLE"
        const val EXTRA_DESCRIPTION = "com.github.gmarcell.todolist.EXTRA_DESCRIPTION"
        const val EXTRA_DUE_TIME = "com.github.gmarcell.todolist.EXTRA_DUE_TIME"
        const val EXTRA_DUE_DATE = "com.github.gmarcell.todolist.EXTRA_DUE_DATE"
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_does)

        if (intent.hasExtra(EXTRA_ID)) {
            title = "Edit Does"
            edit_text_title.setText(intent.getStringExtra(EXTRA_TITLE))
            edit_text_description.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
            edit_text_duetime.text = intent.getStringExtra(EXTRA_DUE_TIME)
            edit_text_duedate.text = intent.getStringExtra(EXTRA_DUE_DATE)
        } else {
            title = "Add Does"
        }

        edit_text_duetime.setOnClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener{ _, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                cal.set(Calendar.SECOND, 0)

                edit_text_duetime.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        edit_text_duedate.setOnClickListener {
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, day)

                edit_text_duedate.text = SimpleDateFormat("dd MMM yyyy").format(cal.time)
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        savebtn.setOnClickListener {
            saveDoes()
        }

        cancelbtn.setOnClickListener {
            val i = Intent(applicationContext, MainActivity::class.java)
            startActivity(i)
        }
    }

    private fun saveDoes() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (edit_text_title.text.toString().trim().isBlank() || edit_text_description.text.toString().trim().isBlank() || edit_text_duetime.text.toString().trim().isBlank()) {
            Toast.makeText(this, "Can not insert empty does!", Toast.LENGTH_SHORT).show()
            return
        }

        val data = Intent().apply {
            putExtra(EXTRA_TITLE, edit_text_title.text.toString())
            putExtra(EXTRA_DESCRIPTION, edit_text_description.text.toString())
            putExtra(EXTRA_DUE_TIME, edit_text_duetime.text.toString())
            putExtra(EXTRA_DUE_DATE, edit_text_duedate.text.toString())
            if (intent.getIntExtra(EXTRA_ID, -1) != -1) {
                putExtra(EXTRA_ID, intent.getIntExtra(EXTRA_ID, -1))
            }
        }

        val notificationIntent = Intent(this, AlarmReceiver::class.java)
        val broadcast = PendingIntent.getBroadcast(
            this,
            100,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, broadcast)
        setResult(Activity.RESULT_OK, data)
        finish()
    }
}
