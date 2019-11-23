package com.github.gmarcell.todolist.notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.gmarcell.todolist.MainActivity
import com.github.gmarcell.todolist.R
import kotlinx.android.synthetic.main.activity_notif.*
import kotlin.random.Random

class NotifActivity : AppCompatActivity() {

    var answer: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notif)

        val v = this.applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val random1 = Random.nextInt(0, 99)
        val random2 = Random.nextInt(0, 99)
        val random3 = Random.nextInt(0, 99)

        answer = random1 + random2 - random3
        question.text = random1.toString() + " + " + random2.toString() + " - " + random3.toString()

        kill.setOnClickListener {
            if (userAns.text.toString() == answer.toString()){
                v.cancel()
                val i = Intent(applicationContext, MainActivity::class.java)
                startActivity(i)
            } else {
                Toast.makeText(it.context, "Wrong Answer!", Toast.LENGTH_LONG).show()
            }
        }
    }
}