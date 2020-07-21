package com.github.gmarcell.todolist

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.appcompat.widget.Toolbar
import com.github.gmarcell.todolist.notification.DetectFace
import com.github.gmarcell.todolist.notification.NotifActivity
import com.pro100svitlo.fingerprintAuthHelper.FahErrorType
import com.pro100svitlo.fingerprintAuthHelper.FahListener
import com.pro100svitlo.fingerprintAuthHelper.FingerprintAuthHelper
import java.util.concurrent.TimeUnit

class FingerprintAct : AppCompatActivity(),
    FahListener {
    private val TIME_OUT = 500
    private var mFAH: FingerprintAuthHelper? = null
    private var mFingerprintIcon: ImageView? = null
    private lateinit var mFingerprintText: TextView
    private var mFingerprintRetryStr: String? = null
    private var mFpColorError = 0
    private var mFpColorNormal = 0
    private var mFpColorSuccess = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fingerprint)
        val toolbar: Toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        mFingerprintIcon = findViewById(R.id.iv_fingerprint) as ImageView?
        mFingerprintText = findViewById(R.id.tv_fingerprintText) as TextView
        mFAH = FingerprintAuthHelper.Builder(this, this)
            .setTryTimeOut(2 * 45 * 1000.toLong())
            .setKeyName(MainActivity::class.java.simpleName)
            .setLoggingEnable(true)
            .build()
        val isHardwareEnable = mFAH!!.isHardwareEnable

        //in case if user want to disable usage fingerprint u can turn if off
//        mFAH.setCanListenByUser(false);
        if (isHardwareEnable && mFAH!!.canListenByUser) {
            mFpColorError = ContextCompat.getColor(this, android.R.color.holo_red_dark)
            mFpColorNormal = ContextCompat.getColor(this, R.color.colorPrimary)
            mFpColorSuccess = ContextCompat.getColor(this, android.R.color.holo_green_dark)
            mFingerprintRetryStr = getString(R.string.fingerprintTryIn)
        } else {
            mFingerprintText.setText(getString(R.string.notSupport))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_finger, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        mFAH!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        mFAH!!.stopListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        mFAH!!.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mFAH!!.onSaveInstanceState(outState!!)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        mFAH!!.onRestoreInstanceState(savedInstanceState!!)
    }

    override fun onFingerprintStatus(
        authSuccessful: Boolean,
        errorType: Int,
        errorMess: CharSequence?
    ) {
        if (authSuccessful) {
            DrawableCompat.setTint(mFingerprintIcon!!.drawable, mFpColorSuccess)
            Handler().postDelayed({ goToSecondActivity() }, TIME_OUT.toLong())
        } else if (mFAH != null) {
            Toast.makeText(this, errorMess, Toast.LENGTH_SHORT).show()
            when (errorType) {
                FahErrorType.General.HARDWARE_DISABLED, FahErrorType.General.NO_FINGERPRINTS -> mFAH!!.showSecuritySettingsDialog()
                FahErrorType.Auth.AUTH_NOT_RECOGNIZED -> {
                    DrawableCompat.setTint(mFingerprintIcon!!.drawable, mFpColorError)
                    Handler().postDelayed({
                        DrawableCompat.setTint(
                            mFingerprintIcon!!.drawable,
                            mFpColorNormal
                        )
                    }, TIME_OUT.toLong())
                }
            }
        }
    }

    override fun onFingerprintListening(
        listening: Boolean,
        milliseconds: Long
    ) {
        if (listening) {
            setFingerprintListening()
        } else {
            setFingerprintNotListening()
        }
        if (milliseconds > 0) {
            mFingerprintText!!.setTextColor(mFpColorError)
            mFingerprintText!!.text = getPrettyTime(mFingerprintRetryStr, milliseconds)
        }
    }

    private fun goToSecondActivity() {
        finish()
        startActivity(Intent(this@FingerprintAct, MainActivity::class.java))
    }

    

    private fun setFingerprintListening() {
        DrawableCompat.setTint(mFingerprintIcon!!.drawable, mFpColorNormal)
        mFingerprintText!!.setTextColor(mFpColorNormal)
        mFingerprintText.setText(getString(R.string.touch_sensor))
    }

    private fun setFingerprintNotListening() {
        mFingerprintText!!.setTextColor(mFpColorError)
        DrawableCompat.setTint(mFingerprintIcon!!.drawable, mFpColorError)
    }

    private fun getPrettyTime(coreStr: String?, millis: Long): String {
        return String.format(
            coreStr!!,
            TimeUnit.MILLISECONDS.toMinutes(millis),
            TimeUnit.MILLISECONDS.toSeconds(millis) -
                    TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(
                            millis
                        )
                    )
        )
    }




}