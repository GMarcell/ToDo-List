package com.github.gmarcell.todolist.Notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.gmarcell.todolist.MainActivity
import com.github.gmarcell.todolist.R
import kotlinx.android.synthetic.main.activity_test_notif.*
import kotlin.random.Random

class testNotif : AppCompatActivity() {

    var answer = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_notif)

        val v = this.applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val random1 = Random.nextInt(0, 9)
        val random2 = Random.nextInt(0, 9)

        answer = random1 + random2
        question.text = random1.toString() + " + " + random2.toString()

        kill.setOnClickListener {
            if (ansUser.text.toString().equals(answer.toString())){
                v.cancel()
                val i = Intent(applicationContext, MainActivity::class.java)
                startActivity(i)
            } else {
                Toast.makeText(applicationContext, "Wrong Answer!", Toast.LENGTH_LONG).show()
            }
        }
    }
}