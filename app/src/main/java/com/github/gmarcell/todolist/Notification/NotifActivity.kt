package com.github.gmarcell.todolist.notification

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.gmarcell.todolist.AddEditDoesActivity
import com.github.gmarcell.todolist.MainActivity
import com.github.gmarcell.todolist.R
import kotlinx.android.synthetic.main.activity_notif.*
import kotlin.random.Random

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class NotifActivity : AppCompatActivity() {

    private var answer: Int = 0
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notif)

        val v = this.applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        var random1 = Random.nextInt(0, 99)
        var random2 = Random.nextInt(0, 99)
        var random3 = Random.nextInt(0, 99)

        val titletext = findViewById<TextView>(R.id.Titletext)
        val desctext = findViewById<TextView>(R.id.Desctext)
        val intent =intent.extras

        val title = intent.getString(AddEditDoesActivity.EXTRA_TITLE)
        val desc = intent.getString(AddEditDoesActivity.EXTRA_DESCRIPTION)
        titletext.text = title
        desctext.text = desc

        while (true){
            if(random1 + random2 < random3){
                random1 = Random.nextInt(0, 99)
                random2 = Random.nextInt(0, 99)
                random3 = Random.nextInt(0, 99)
            }else{
                answer = random1 + random2 - random3
                question.text = "$random1 + $random2 - $random3"
                break
            }
        }

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