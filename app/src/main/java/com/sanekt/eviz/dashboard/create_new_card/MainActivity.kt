package com.sanekt.eviz.dashboard.create_new_card

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Layout
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.sanekt.eviz.R
import com.sanekt.eviz.dashboard.SaveCardActivity
import com.sanekt.eviz.dashboard.model.ImageModel
import com.sanekt.eviz.dashboard.model.TextModel
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.lang.StringBuilder
import java.util.*

open class MainActivity : AppCompatActivity() {
    private var stickerView: StickerView? = null
    private var saveCardBtn: Button? = null
    private var sticker: TextSticker? = null
    open var sticker1: TextSticker? = null
    private var sticker2: TextSticker? = null
    private var sticker3: TextSticker? = null
    private var stickerImage: DrawableSticker1? = null
    private var uri: String? = "file:///storage/emulated/0/DCIM/Camera/IMG_20200906_094422741.jpg"
    private var json: String? = ""
    private var jsonData: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stickerView = findViewById(R.id.sticker_view)
        saveCardBtn = findViewById(R.id.saveCardId)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        //currently you can config your own icons and icon event
        //the event you can custom
        val deleteIcon = BitmapStickerIcon(
            ContextCompat.getDrawable(
                this,
                R.drawable.edit_icon1
            ),
            BitmapStickerIcon.RIGHT_TOP, this
        )
        deleteIcon.iconEvent = DeleteIconEvent(this)
        val zoomIcon = BitmapStickerIcon(
            ContextCompat.getDrawable(
                this,
                R.drawable.sticker_ic_scale_white_18dp
            ),
            BitmapStickerIcon.RIGHT_BOTOM,
            this
        )
        zoomIcon.iconEvent = ZoomIconEvent()
        stickerView!!.icons = Arrays.asList(deleteIcon, zoomIcon)
        stickerView!!.setBackgroundColor(Color.WHITE)
        stickerView!!.isLocked = false
        stickerView!!.isConstrained = true
        stickerView!!.onStickerOperationListener = object : StickerView.OnStickerOperationListener {
            override fun onStickerAdded(sticker: Sticker) {
                Log.d(TAG, "onStickerAdded")
            }

            override fun onStickerClicked(sticker: Sticker) { //stickerView.removeAllSticker();
                if (sticker is DrawableSticker1) {
                    stickerImage?.matrix = sticker.matrix
                }
            }

            override fun onStickerDeleted(sticker: Sticker) {
                Log.d(TAG, "onStickerDeleted")
            }

            override fun onStickerDragFinished(sticker: Sticker) {
                Log.d(TAG, "onStickerDragFinished")
                if (sticker is DrawableSticker1) {
                    stickerImage?.matrix = sticker.matrix
                }
            }

            override fun onStickerTouchedDown(sticker: Sticker) {
                if (sticker is DrawableSticker1) {
                    stickerImage?.matrix = sticker.matrix
                }
            }

            override fun onStickerZoomFinished(sticker: Sticker) {
                Log.d(TAG, "onStickerZoomFinished")
                if (sticker is DrawableSticker1) {
                    stickerImage?.matrix = sticker.matrix
                }
            }

            override fun onStickerFlipped(sticker: Sticker) {
                Log.d(TAG, "onStickerFlipped")
                if (sticker is DrawableSticker1) {
                    stickerImage?.matrix = sticker.matrix
                }
            }

            override fun onStickerDoubleTapped(sticker: Sticker) {
                Log.d(TAG, "onDoubleTapped: double tap will be with two click")
                if (sticker is DrawableSticker1) {
                    stickerImage?.matrix = sticker.matrix
                }
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERM_RQST_CODE
            )
        } else {
            testAdd(TextSticker(this), "Radha", 150f, -20f)
            testAdd2(TextSticker(this), "Lead Manager", 150f, 80f)
            testAdd3(TextSticker(this), "radha@gmail.com", 150f, 180f)
            loadSticker()
        }
        saveCardId.setOnClickListener {
            jsonData = createJson()
            val sendIntent = Intent(this, SaveCardActivity()::class.java)
            sendIntent.putExtra(
                "data", jsonData
            )
            startActivity(sendIntent)
            finish()
//            var returnIntent = Intent()
//            returnIntent.putExtra("result", jsonData)
//            setResult(Activity.RESULT_OK, returnIntent)
//            finish()
        }
    }

    private fun createJson(
    ): String {
        var matrix1 = stickerImage!!.matrix
        var matrix2 = sticker1!!.matrix
        var matrix3 = sticker2!!.matrix
        var matrix4 = sticker3!!.matrix
        var gson = Gson()
        var builder1 = getImageData(matrix1, stickerImage!!)
        var builder2 = getTextData(matrix2, sticker1!!)
        var builder3 = getTextData(matrix3, sticker2!!)
        var builder4 = getTextData(matrix4, sticker3!!)
        var matrixJson1 = gson.toJson(builder1)
        var matrixJson2 = gson.toJson(builder2)
        var matrixJson3 = gson.toJson(builder3)
        var matrixJson4 = gson.toJson(builder4)

        var image =
            ImageModel(matrixJson1, uri)
        var text1 = TextModel(
            matrixJson2,
            sticker1?.text, sticker1!!.getTextColor(),
            sticker1?.getTypeFaceNo()
        )
        var text2 = TextModel(
            matrixJson3,
            sticker2?.text,
            sticker2?.getTextColor(),
            sticker2?.getTypeFaceNo()
        )
        var text3 = TextModel(
            matrixJson4,
            sticker3?.text,
            sticker3?.getTextColor(),
            sticker3?.getTypeFaceNo()
        )
        var card = Card(
            image,
            text1,
            text2,
            text3
        )
        try {
            var gson = Gson()
            json = gson.toJson(card)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return json.toString()
    }

    private fun getTextData(
        matrix1: Matrix,
        sticker1: TextSticker
    ): String {
        var builder: StringBuilder? = StringBuilder()
        var array: FloatArray? = FloatArray(9)
        if (array != null) {
            for (i in 0..8) {
                array[i] = sticker1?.getMatrixValue(matrix1, i)!!
                builder?.append(array[i].toString() + " ")
            }
        }
        var stringArray: List<String> = builder.toString().split(" ")
        var floatArray: FloatArray? = FloatArray(9)
        for (i in 0..8) {
            floatArray?.set(i, stringArray[i].toFloat())
        }
        return builder.toString()
    }

    private fun getImageData(
        matrix1: Matrix,
        stickerImage: DrawableSticker1
    ): String {
        var builder: StringBuilder? = StringBuilder()
        var array: FloatArray? = FloatArray(9)
        if (array != null) {
            for (i in 0..8) {
                array[i] = stickerImage?.getMatrixValue(matrix1, i)!!
                builder?.append(array[i].toString() + " ")
            }
        }
        var stringArray: List<String> = builder.toString().split(" ")
        var floatArray: FloatArray? = FloatArray(9)
        for (i in 0..8) {
            floatArray?.set(i, stringArray[i].toFloat())
        }
        return builder.toString()
    }

    private fun loadSticker() {
        val drawable = ContextCompat.getDrawable(this, R.drawable.logo)
//        stickerImage = drawable?.let { DrawableSticker1(this, "uri", "") }
        stickerImage = drawable?.let {
            DrawableSticker1(
                this,
                "uri",
                "file:///storage/emulated/0/DCIM/Camera/IMG_20200906_094422741.jpg"
            )
        }
        stickerImage?.matrix?.postTranslate(120f, 80f)
//        stickerImage?.matrix?.postScale(120f, 80f)
        if (stickerImage != null) {
            stickerView!!.addSticker(stickerImage!!, Sticker.Position.TOP, "", "")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERM_RQST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadSticker()
            testAdd(TextSticker(this), "Radha", 150f, -20f)
            testAdd2(TextSticker(this), "Lead Manager", 150f, 80f)
            testAdd3(TextSticker(this), "radha@gmail.com", 150f, 180f)
        }
    }

    fun testReplace(view: View?) {
        if (stickerView!!.replace(sticker)) {
            Toast.makeText(this@MainActivity, "Replace Sticker successfully!", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(this@MainActivity, "Replace Sticker failed!", Toast.LENGTH_SHORT).show()
        }
    }

    fun testLock(view: View?) {
        stickerView!!.isLocked = !stickerView!!.isLocked
    }

    fun testRemove(view: View?) {
        if (stickerView!!.removeCurrentSticker()) {
            Toast.makeText(
                this@MainActivity,
                "Remove current Sticker successfully!",
                Toast.LENGTH_SHORT
            )
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
        testAdd(TextSticker(this), "Radha", 150f, -20f)
        testAdd2(TextSticker(this), "Lead Manager", 150f, 80f)
        testAdd3(TextSticker(this), "radha@gmail.com", 150f, 180f)
    }

    fun testAdd(sticker: TextSticker, text: String, xAxis: Float, yAxis: Float) {
        sticker1 = sticker
        sticker1!!.text = text
        sticker1!!.setTextAlign(Layout.Alignment.ALIGN_CENTER)
        sticker1!!.resizeText()
        sticker1!!.matrix.postTranslate(xAxis, yAxis)
        stickerView!!.addSticker(sticker1!!, Sticker.Position.CENTER, "", "1")
    }

    fun testAdd2(sticker: TextSticker, text: String, xAxis: Float, yAxis: Float) {
        sticker2 = sticker
        sticker2!!.text = text
        sticker2!!.setTextAlign(Layout.Alignment.ALIGN_CENTER)
        sticker2!!.resizeText()
        sticker2!!.matrix.postTranslate(xAxis, yAxis)
        stickerView!!.addSticker(sticker2!!, Sticker.Position.CENTER, "", "2")
    }

    fun testAdd3(sticker: TextSticker, text: String, xAxis: Float, yAxis: Float) {
        sticker3 = sticker
        sticker3!!.text = text
        sticker3!!.setTextAlign(Layout.Alignment.ALIGN_CENTER)
        sticker3!!.resizeText()
        sticker3!!.matrix.postTranslate(xAxis, yAxis)
        stickerView!!.addSticker(sticker3!!, Sticker.Position.CENTER, "", "3")
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        const val PERM_RQST_CODE = 110
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                Log.e("TAG", "Path:${ImagePicker.getFilePath(data)}")
                // File object will not be null for RESULT_OK
                val file = ImagePicker.getFile(data)!!
                when (requestCode) {
                    101 -> {
                        var uri4 = Uri.fromFile(File(file.toString()))
                        stickerView?.replace(DrawableSticker1(this, "uri", uri4.toString()), true)
                        uri = uri4.toString()
                    }
                }
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    class DeleteIconEvent(mainActivity: MainActivity) : StickerIconEvent {
        var context = mainActivity

        override fun onActionDown(stickerView: StickerView?, event: MotionEvent?) {}
        override fun onActionMove(stickerView: StickerView?, event: MotionEvent?) {}
        override fun onActionUp(stickerView: StickerView?, event: MotionEvent?) {
            if (stickerView?.currentSticker is TextSticker) {
                var sticker = stickerView.currentSticker
//            openColorPickerDialog(sticker,stickerView)
                customDialog(sticker, stickerView)
            } else {
                pickProfileImage()
            }
        }

        private fun openColorPickerDialog(
            sticker: Sticker?,
            stickerView: StickerView
        ) {
            ColorPickerDialogBuilder
                .with(context)
                .setTitle("Choose color")
                .initialColor(context.resources.getColor(R.color.white))
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener { selectedColor ->
                    Toast.makeText(context, "hello" + selectedColor, Toast.LENGTH_SHORT).show()
                    if (sticker is TextSticker) {
                        sticker.setTextColor(selectedColor)
                        stickerView!!.replace(sticker)
                        stickerView!!.invalidate()
                    }
                }
                .setPositiveButton(
                    "ok"
                ) { dialog, selectedColor, allColors ->
                    Toast.makeText(context, "hello" + selectedColor, Toast.LENGTH_SHORT).show()
                    if (sticker is TextSticker) {
                        sticker.setTextColor(selectedColor)
                        stickerView!!.replace(sticker)
                        stickerView!!.invalidate()
                    }
                }
                .setNegativeButton(
                    "cancel"
                ) { dialog, which -> }
                .build()
                .show()
        }

        fun pickProfileImage() {
            ImagePicker.with(context)
                // Crop Square image
                .crop()
                .setImageProviderInterceptor { imageProvider ->
                    // Intercept ImageProvider
                    Log.d("ImagePicker", "Selected ImageProvider: " + imageProvider.name)
                }
                // Image resolution will be less than 512 x 512
                .maxResultSize(512, 512)
                .start(PROFILE_IMAGE_REQ_CODE)
        }

        companion object {
            private const val PROFILE_IMAGE_REQ_CODE = 101
        }

        private fun customDialog(
            sticker1: Sticker?,
            stickerView: StickerView
        ) {

            var color = ""
            val dialog = Dialog(context)
            var data1 = sticker1?.dataType
            dialog.setCancelable(true)
            var textSticker = sticker1 as TextSticker
            var data: String? = textSticker.text
            var textColor: Int? = textSticker.getTextColor()
            dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.window!!.setContentView(R.layout.new_dialog)

            val ivClose = dialog.findViewById<ImageView>(R.id.iv_close)
            val etText = dialog.findViewById<EditText>(R.id.et_text)
            val tvSave = dialog.findViewById<TextView>(R.id.tv_save)
            val spinner = dialog.findViewById<Spinner>(R.id.spinner)
            var selectColor = 0
            etText?.setText(data)

            val colorPickerView: ColorPickerView = dialog.findViewById(R.id.color_picker_view)

            colorPickerView.addOnColorChangedListener { selectedColor ->
                // Handle on color change
                color = "ColorPicker onColorChanged: 0x" + Integer.toHexString(selectedColor)
                selectColor = selectedColor

            }
            colorPickerView.addOnColorSelectedListener { selectedColor ->
                color = "SelectedColor onColorChanged: 0x" + Integer.toHexString(selectedColor)
                selectColor = selectedColor
            }
            var typeCount = 0
            spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    typeCount = position + 1
                }

            }
            ivClose.setOnClickListener { dialog.dismiss() }
            tvSave.setOnClickListener {
                var data = ""
                if (!TextUtils.isEmpty(etText?.text.toString()))
                    data = etText?.text.toString()
                val sticker = TextSticker(context)
                sticker.text = data
                sticker.text1 = data
                if (selectColor != 0) {
                    sticker.setTextColor(selectColor)
                } else {
                    if (textColor != null) {
                        sticker.setTextColor(textColor)
                    }
                }
                sticker.setTypefaceNo(typeCount)
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
                var matrix1 = sticker1?.matrix
                if (matrix1 != null) {
                    sticker.matrix = matrix1
                }
                if (data1 == "1") {
                    context.testAdd(sticker, matrix1, stickerView)
                } else if (data1 == "2") {
                    context.testAdd1(sticker, matrix1, stickerView)
                } else if (data1 == "3") {
                    context.testAdd2(sticker, matrix1, stickerView)
                }
                dialog.dismiss()
            }
            dialog.show()
        }

    }

    private fun testAdd(
        sticker11: TextSticker,
        matrix: Matrix,
        stickerView: StickerView
    ) {
        sticker1!!.text = sticker11.text
        sticker1!!.setTextColor(sticker11.getTextColor())
        sticker1!!.setTextAlign(Layout.Alignment.ALIGN_CENTER)
        sticker1!!.setTypeface(sticker11.getTypeFace())
        sticker1!!.setTypefaceNo(sticker11.getTypeFaceNo())
        sticker1!!.resizeText()
        sticker1!!.matrix = matrix
        stickerView!!.replace(sticker1, true)
    }

    private fun testAdd1(
        sticker11: TextSticker,
        matrix: Matrix,
        stickerView: StickerView
    ) {
        sticker2!!.text = sticker11.text
        sticker2!!.setTextColor(sticker11.getTextColor())
        sticker2!!.setTextAlign(Layout.Alignment.ALIGN_CENTER)
        sticker2!!.setTypeface(sticker11.getTypeFace())
        sticker2!!.setTypefaceNo(sticker11.getTypeFaceNo())
        sticker2!!.resizeText()
        sticker2!!.matrix = matrix
        stickerView!!.replace(sticker2, true)
    }

    private fun testAdd2(
        sticker11: TextSticker,
        matrix: Matrix,
        stickerView: StickerView
    ) {
        sticker3!!.text = sticker11.text
        sticker3!!.setTextColor(sticker11.getTextColor())
        sticker3!!.setTextAlign(Layout.Alignment.ALIGN_CENTER)
        sticker3!!.setTypeface(sticker11.getTypeFace())
        sticker3!!.setTypefaceNo(sticker11.getTypeFaceNo())
        sticker3!!.resizeText()
        sticker3!!.matrix = matrix
        stickerView!!.replace(sticker3, true)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}