package com.github.gmarcell.todolist.notification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.github.gmarcell.todolist.R

class KillChoice : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kill_choice)

        val mathProb = findViewById<Button>(R.id.math)
        val takePhoto = findViewById<Button>(R.id.photo)
        val intent =intent.extras

        mathProb.setOnClickListener{
            val intentmath = Intent(this, NotifActivity::class.java)
            intentmath.putExtras(intent)
            startActivity(intentmath)
        }

        takePhoto.setOnClickListener{
            val intentface = Intent(this, DetectFace::class.java)
            intentface.putExtras(intent)
            startActivity(intentface)
        }
    }
}