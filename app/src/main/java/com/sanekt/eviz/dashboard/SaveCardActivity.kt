package com.sanekt.eviz.dashboard

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap.CompressFormat
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Typeface
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.sanekt.eviz.R
import com.sanekt.eviz.dashboard.create_new_card.*
import com.sanekt.eviz.dashboard.model.ImageModel
import com.sanekt.eviz.dashboard.model.TextModel
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream


class SaveCardActivity() : AppCompatActivity() {

    private var card: Card? = null
    private var gson = Gson()
    private var json: String? = null
    private var a: String? = null
    private var stickerView: StickerView? = null
    private var context: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_card)
        context = this
        stickerView = findViewById(R.id.sticker_view)
        stickerView!!.setBackgroundColor(Color.WHITE)
        stickerView!!.isLocked = true
        stickerView!!.isConstrained = true
//        val card1 = gson.fromJson(intent.getStringExtra("matrix"), Matrix::class.java)

        stickerView!!.onStickerOperationListener = object : StickerView.OnStickerOperationListener {
            override fun onStickerAdded(sticker: Sticker) {
                Log.d("", "onStickerAdded")
            }

            override fun onStickerClicked(sticker: Sticker) { //stickerView.removeAllSticker();
            }

            override fun onStickerDeleted(sticker: Sticker) {
                Log.d("", "onStickerDeleted")
            }

            override fun onStickerDragFinished(sticker: Sticker) {
                Log.d("", "onStickerDragFinished")
            }

            override fun onStickerTouchedDown(sticker: Sticker) {
            }

            override fun onStickerZoomFinished(sticker: Sticker) {
                Log.d("", "onStickerZoomFinished")
            }

            override fun onStickerFlipped(sticker: Sticker) {
                Log.d("", "onStickerFlipped")
            }

            override fun onStickerDoubleTapped(sticker: Sticker) {
                Log.d("", "onDoubleTapped: double tap will be with two click")
            }
        }
        loadSticker()
        saveCardId.setOnClickListener {
            var count = countFiles(this.getExternalFilesDir(null)!!, 0)
            val imageView: View = findViewById<View>(R.id.relativeLayout) as RelativeLayout
            imageView.isDrawingCacheEnabled = true
            imageView.buildDrawingCache(true)
            val imageFile = File(
                this.getExternalFilesDir(null),
                "Card" + count + ".jpg"
            )
            val fileOutputStream = FileOutputStream(imageFile)
            imageView.getDrawingCache(true).compress(
                CompressFormat.JPEG, 100,
                fileOutputStream
            )
            fileOutputStream.close()
            imageView.isDrawingCacheEnabled = false
            Toast.makeText(this, "Your card has been saved.", Toast.LENGTH_SHORT).show()
            val sendIntent = Intent(this, DashBoardActivity()::class.java)
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(sendIntent)
            finish()
        }
    }

    fun countFiles(folder: File, count1: Int?): Int? {
        var count: Int = 0
        if (count1 != null) {
            count = count1
        }
        val files = this.getExternalFilesDir(null)?.listFiles()
        for (file in files!!) {
            if (file.isFile) {
                count++
            } else {
                countFiles(file, count)
            }
        }
        return count
    }

    private fun loadSticker() {
        var string = intent.getStringExtra("data")
        //var string = "{\"imageData\":{\"matrix1\":\"\\\"2.2561898 0.67032677 23.674162 -0.67032677 2.2561898 50.706844 0.0 0.0 1.0 \\\"\",\"uriString\":\"file:///storage/emulated/0/DCIM/Camera/IMG_20200906_094422741.jpg\"},\"textData1\":{\"matrix1\":\"\\\"1.0 0.0 150.0 0.0 1.0 -20.0 0.0 0.0 1.0 \\\"\",\"textColor\":-16777216,\"textName\":\"Radha\",\"typeface\":0},\"textData2\":{\"matrix1\":\"\\\"1.0 0.0 150.0 0.0 1.0 80.0 0.0 0.0 1.0 \\\"\",\"textColor\":-16777216,\"textName\":\"Lead\",\"typeface\":1},\"textData3\":{\"matrix1\":\"\\\"1.0 0.0 150.0 0.0 1.0 180.0 0.0 0.0 1.0 \\\"\",\"textColor\":-16777216,\"textName\":\"radha@gmail.com\",\"typeface\":0}}"
        card = gson?.fromJson(string, Card::class.java)
        val gson = Gson()
        val posts = gson.fromJson(string, Card::class.java)
        try {
            loadSticker2()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadSticker2() {


        var data1 = card?.imageData
        var data2 = card?.textData1
        var data3 = card?.textData2
        var data4 = card?.textData3
        var matrix=getImageData(data1)
        var matrix2=getTextData(data2)
        var matrix3=getTextData(data3)
        var matrix4=getTextData(data4)
        testAdd(matrix3, data3?.textName, data3?.textColor, data3?.typeface)

        val drawable = ContextCompat.getDrawable(this, R.drawable.logo)
        var stickerImage = drawable?.let {
            DrawableSticker1(
                this,
                "uri",
                data1?.uriString!!
            )
        }
        if (matrix != null) {
            stickerImage?.matrix = matrix
        }
        if (stickerImage != null) {
            stickerView!!.addSticker(stickerImage!!, Sticker.Position.TOP, "", "")
        }
        if (matrix2 != null) {
            testAdd(matrix2, data2?.textName, data2?.textColor, data2?.typeface)
        }
        if (matrix3 != null) {
            testAdd(matrix3, data3?.textName, data3?.textColor, data3?.typeface)
        }
        if (matrix4 != null) {
            testAdd(matrix4, data4?.textName, data4?.textColor, data4?.typeface)
        }
    }

    private fun getImageData(data1: ImageModel?):Matrix {
        var string = data1?.matrix1
        var stringArray: List<String> = string?.split(" ")!!
        var floatArray:FloatArray?= FloatArray(9)
        for (i in 0..8) {
            floatArray?.set(i, stringArray[i].replace("\"", "").toFloat())
        }
        floatArray?.size
        var matrix1:Matrix?=Matrix()
        matrix1?.setValues(floatArray)
        return matrix1!!
    }

    private fun getTextData(data: TextModel?):Matrix {
        var string = data?.matrix1
        var stringArray: List<String> = string?.split(" ")!!
        var floatArray:FloatArray?= FloatArray(9)
        for (i in 0..8) {
            floatArray?.set(i, stringArray[i].replace("\"", "").toFloat())
        }
        floatArray?.size
        var matrix1:Matrix?=Matrix()
        matrix1?.setValues(floatArray)
        return matrix1!!
    }

    fun testAdd(
        matrix: Matrix,
        text: String?,
        textColor: Int?,
        typeCount: Int?
    ) {
        var sticker = TextSticker(this)
        sticker!!.text = text
        if (textColor != null) {
            sticker!!.setTextColor(textColor)
        }
        sticker!!.setTextAlign(Layout.Alignment.ALIGN_CENTER)
        sticker!!.resizeText()
        sticker!!.matrix = matrix
        if (typeCount == 1) {
            sticker.setTypeface(Typeface.DEFAULT)

        } else if (typeCount == 2) {
            sticker.setTypeface(Typeface.MONOSPACE)

        } else if (typeCount == 3) {
            sticker.setTypeface(Typeface.DEFAULT_BOLD)

        } else if (typeCount == 4) {
            sticker.setTypeface(Typeface.SANS_SERIF)

        } else if (typeCount == 5) {
            sticker.setTypeface(Typeface.SERIF)

        }
        stickerView!!.addSticker(sticker!!, Sticker.Position.CENTER, "", "")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val sendIntent = Intent(this, MainActivity()::class.java)
//        sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//        sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(sendIntent)
        finish()
    }
}