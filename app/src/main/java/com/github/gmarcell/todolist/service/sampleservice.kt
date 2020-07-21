package com.github.gmarcell.todolist.service

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.LifecycleService
import com.github.gmarcell.todolist.notification.NotifActivity


class sampleservice : LifecycleService() {

    var vibrator: Vibrator? = null
    @SuppressLint("MissingSuperCall")
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        startForegroundService(Intent(this, NotifActivity::class.java))
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    @SuppressLint("MissingSuperCall")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int{
        val a = longArrayOf(100, 1000, 1000)
        vibrator!!.vibrate(VibrationEffect.createWaveform(a, 0))
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        vibrator!!.cancel()
    }
}