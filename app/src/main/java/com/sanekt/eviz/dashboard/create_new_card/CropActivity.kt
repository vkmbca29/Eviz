package com.sanekt.eviz.dashboard.create_new_card

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.github.dhaval2404.imagepicker.ImagePicker
import com.sanekt.eviz.R
import java.io.File

class CropActivity : AppCompatActivity() {
private lateinit var context:Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop)
        context=this
        pickProfileImage()
    }
    fun pickProfileImage() {
        ImagePicker.with(this)
            // Crop Square image
            .crop()
            .setImageProviderInterceptor { imageProvider -> // Intercept ImageProvider
                Log.d("ImagePicker", "Selected ImageProvider: "+imageProvider.name)
            }
            // Image resolution will be less than 512 x 512
            .maxResultSize(512, 512)
            .start(PROFILE_IMAGE_REQ_CODE)
    }

    companion object {
        private const val PROFILE_IMAGE_REQ_CODE = 101
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            Log.e("TAG", "Path:${ImagePicker.getFilePath(data)}")
            // File object will not be null for RESULT_OK
            val file = ImagePicker.getFile(data)!!
            when (requestCode) {
                PROFILE_IMAGE_REQ_CODE -> {
                    var mProfileFile = file
                    Toast.makeText(this, mProfileFile.toString(), Toast.LENGTH_SHORT).show()
                    var uri= Uri.parse(file.toString())
                    var uri4=Uri.fromFile(File(file.toString()))

                    var uri1= Uri.parse(file.toString())

//                    imgProfile.setLocalImage(file, true)
                }
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}
