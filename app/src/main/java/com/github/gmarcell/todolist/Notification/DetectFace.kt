package com.github.gmarcell.todolist.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.gmarcell.todolist.AddEditDoesActivity
import com.github.gmarcell.todolist.MainActivity
import com.github.gmarcell.todolist.R
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.FaceDetector
import kotlinx.android.synthetic.main.activity_take__picture.*


class DetectFace : AppCompatActivity() {

    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take__picture)

        val vibrate = this.applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        val capture_btn = findViewById<Button>(R.id.capture_btn)
        val check_btn = findViewById<Button>(R.id.check_btn)

        val titletext = findViewById<TextView>(R.id.Titletext)
        val desctext = findViewById<TextView>(R.id.Desctext)
        val intent =intent.extras
        val title = intent.getString(AddEditDoesActivity.EXTRA_TITLE)
        val desc = intent.getString(AddEditDoesActivity.EXTRA_DESCRIPTION)
        titletext.text = title
        desctext.text = desc

        //button click
        capture_btn.setOnClickListener {
            //if system os is Marshmallow or Above, we need to request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED
                ) {
                    //permission was not enabled
                    val permission = arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                } else {
                    //permission already granted
                    openCamera()
                }
            } else {
                //system os is < marshmallow
                openCamera()
            }
        }
        check_btn.setOnClickListener (View.OnClickListener{ v ->
            val options = BitmapFactory.Options()
            options.inMutable = true
            val myBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, image_uri)
            val myRectPaint = Paint()
            myRectPaint.strokeWidth = 10f
            myRectPaint.color = Color.RED
            myRectPaint.style = Paint.Style.STROKE
            val tempBitmap = Bitmap.createBitmap(myBitmap.width, myBitmap.height, Bitmap.Config.RGB_565)
            val tempCanvas = Canvas(tempBitmap)
            tempCanvas.drawBitmap(myBitmap, 0f, 0f, null)
            val faceDetector = FaceDetector.Builder(applicationContext).setTrackingEnabled(false)
                .build()
            if (!faceDetector.isOperational) {
                AlertDialog.Builder(v.context).setMessage("Could not set up the face detector!").show()
                return@OnClickListener
            }
            val frame = Frame.Builder().setBitmap(myBitmap).build()
            val faces = faceDetector.detect(frame)
            val jumlah = faces.size()
            if (jumlah == 0){
                Toast.makeText(this, "No Face Detected", Toast.LENGTH_SHORT).show()
            }
            else{
                //Toast.makeText(this, jumlah.toString(), Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "Nice Face", Toast.LENGTH_SHORT).show()
                vibrate.cancel()
                val intentini = Intent(this, MainActivity::class.java)
                startActivity(intentini)
            }

//            for (i in 0 until faces.size()) {
//                val thisFace = faces.valueAt(i)
//                val x1 = thisFace.position.x
//                val y1 = thisFace.position.y
//                val x2 = x1 + thisFace.width
//                val y2 = y1 + thisFace.height
//                tempCanvas.drawRoundRect(RectF(x1, y1, x2, y2), 2f, 2f, myRectPaint)
//            }
//            myImageView.setImageDrawable(BitmapDrawable(resources, tempBitmap))

//            val jumlah = faces.size().toString()
//            Toast.makeText(this, jumlah, Toast.LENGTH_SHORT).show()
        })
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //called when user presses ALLOW or DENY from Permission Request Popup
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted
                    openCamera()
                }
                else{
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //called when image was captured from camera intent
        if (resultCode == Activity.RESULT_OK){
            //set image captured to image view
            image_view.setImageURI(image_uri)
        }
    }
}
