package com.sanekt.eviz

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.text.Layout
import android.util.Log
import android.view.View
import android.widget.Toast
import com.sanekt.eviz.dashboard.create_new_card.BitmapStickerIcon
import com.sanekt.eviz.dashboard.create_new_card.StickerView
import com.sanekt.eviz.dashboard.create_new_card.TextSticker
import com.sanekt.eviz.dashboard.create_new_card.*
import com.sanekt.eviz.FileUtil.getNewFile
import com.github.dhaval2404.imagepicker.ImagePicker
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {
    private var stickerView: StickerView? = null
    private var sticker: TextSticker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        stickerView = findViewById(R.id.sticker_view) as StickerView
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        //currently you can config your own icons and icon event
        //the event you can custom
        val deleteIcon = BitmapStickerIcon(ContextCompat.getDrawable(this,
                R.drawable.edit_icon1),
                BitmapStickerIcon.RIGHT_TOP,this)
        deleteIcon.iconEvent = DeleteIconEvent(this)
        val zoomIcon = BitmapStickerIcon(
            ContextCompat.getDrawable(this,
                    R.drawable.sticker_ic_scale_white_18dp),
            BitmapStickerIcon.RIGHT_BOTOM,
            this
        )
        zoomIcon.iconEvent = ZoomIconEvent()
        stickerView!!.icons = Arrays.asList(deleteIcon, zoomIcon)
        stickerView!!.setBackgroundColor(Color.WHITE)
        stickerView!!.isLocked = false
        stickerView!!.isConstrained = true
//        sticker = TextSticker(this)
//        sticker!!.text = "Hello, world!"
//        sticker!!.setTextColor(Color.BLACK)
//        sticker!!.setTextAlign(Layout.Alignment.ALIGN_OPPOSITE)
//        sticker!!.resizeText()
        stickerView!!.onStickerOperationListener = object : StickerView.OnStickerOperationListener {
            override fun onStickerAdded(sticker: Sticker) {
                Log.d(TAG, "onStickerAdded")
            }

            override fun onStickerClicked(sticker: Sticker) { //stickerView.removeAllSticker();
            }

            override fun onStickerDeleted(sticker: Sticker) {
                Log.d(TAG, "onStickerDeleted")
            }

            override fun onStickerDragFinished(sticker: Sticker) {
                Log.d(TAG, "onStickerDragFinished")
            }

            override fun onStickerTouchedDown(sticker: Sticker) {
            }

            override fun onStickerZoomFinished(sticker: Sticker) {
                Log.d(TAG, "onStickerZoomFinished")
            }

            override fun onStickerFlipped(sticker: Sticker) {
                Log.d(TAG, "onStickerFlipped")
            }

            override fun onStickerDoubleTapped(sticker: Sticker) {
                Log.d(TAG, "onDoubleTapped: double tap will be with two click")
            }
        }
        if (toolbar != null) {
            toolbar.setTitle(R.string.app_name)
            toolbar.inflateMenu(R.menu.menu_save)
            toolbar.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.item_save) {
                    val file = getNewFile(this@MainActivity, "Sticker")
                    if (file != null) {
                        stickerView!!.save(file)
                        Toast.makeText(this@MainActivity, "saved in " + file.absolutePath,
                                Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "the file is null", Toast.LENGTH_SHORT).show()
                    }
                }
                //                    stickerView.replace(new DrawableSticker(
//                            ContextCompat.getDrawable(MainActivity.this, R.drawable.haizewang_90)
//                    ));
                false
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERM_RQST_CODE)
        } else {
            loadSticker()
            testAdd("Radha",150f,-120f)
            testAdd("Lead Manager",150f,-20f)
            testAdd("radha@gmail.com",150f,80f)
        }
    }

    private fun loadSticker() {
        val drawable = ContextCompat.getDrawable(this, R.drawable.logo)
        var sticker: DrawableSticker? = drawable?.let { DrawableSticker(it,this,"logo","") }
        sticker?.matrix?.postTranslate(-150f,30f)
        if (sticker != null) {
            stickerView!!.addSticker(sticker, Sticker.Position.TOP)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERM_RQST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadSticker()
            testAdd("Radha",150f,-120f)
            testAdd("Lead Manager",150f,-20f)
            testAdd("radha@gmail.com",150f,80f)
        }
    }

    fun testReplace(view: View?) {
        if (stickerView!!.replace(sticker)) {
            Toast.makeText(this@MainActivity, "Replace Sticker successfully!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@MainActivity, "Replace Sticker failed!", Toast.LENGTH_SHORT).show()
        }
    }

    fun testLock(view: View?) {
        stickerView!!.isLocked = !stickerView!!.isLocked
    }

    fun testRemove(view: View?) {
        if (stickerView!!.removeCurrentSticker()) {
            Toast.makeText(this@MainActivity, "Remove current Sticker successfully!", Toast.LENGTH_SHORT)
                    .show()
        } else {
            Toast.makeText(this@MainActivity, "Remove current Sticker failed!", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    fun testRemoveAll(view: View?) {
        stickerView!!.removeAllStickers()
    }

    fun reset(view: View?) {
        stickerView!!.removeAllStickers()
        loadSticker()
        testAdd("Radha",150f,-120f)
        testAdd("Lead Manager",150f,-20f)
        testAdd("radha@gmail.com",150f,80f)
    }

    fun testAdd(text:String,xAxis:Float,yAxis:Float) {
        val sticker = TextSticker(this)
        sticker.text = text
        sticker.setTextColor(Color.BLUE)
        sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER)
        sticker.resizeText()
        sticker.matrix.postTranslate(xAxis,yAxis)
        stickerView!!.addSticker(sticker,Sticker.Position.CENTER)
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        const val PERM_RQST_CODE = 110
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            Log.e("TAG", "Path:${ImagePicker.getFilePath(data)}")
            // File object will not be null for RESULT_OK
            val file = ImagePicker.getFile(data)!!
            when (requestCode) {
                101 -> {
                    var mProfileFile = file
                    Toast.makeText(this, mProfileFile.toString(), Toast.LENGTH_SHORT).show()
                    var uri4= Uri.fromFile(File(file.toString()))
                    var drawable = ContextCompat.getDrawable(this, R.drawable.logo);
                    drawable?.let { DrawableSticker(drawable,this,"uri",uri4.toString()) }?.let {
                        stickerView!!.replace(it, true)
                    }
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