package com.sanekt.eviz.dashboard.create_new_card

import android.app.Dialog
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Typeface
import android.text.Layout
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.*
import com.sanekt.eviz.R
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.github.dhaval2404.imagepicker.ImagePicker


/**
 * @author wupanjie
 */
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
//                    stickerView!!.icons = Arrays.asList(zoomIcon)
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
              typeCount=position+1
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
            sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER)
            sticker.resizeText()
            if(typeCount==1){
                sticker.setTypeface(Typeface.DEFAULT)

            }else if(typeCount==2){
                sticker.setTypeface(Typeface.MONOSPACE)

            }else if(typeCount==3){
                sticker.setTypeface(Typeface.DEFAULT_BOLD)

            }else if(typeCount==4){
                sticker.setTypeface(Typeface.SANS_SERIF)

            }else if(typeCount==5){
                sticker.setTypeface(Typeface.SERIF)

            }
//            sticker.setTypeface(Typeface.DEFAULT_BOLD)
            var matrix1 = sticker1?.matrix
            if (matrix1 != null) {
                sticker.matrix = matrix1
            }
//            stickerView!!.replace(sticker, true)
            testAdd(sticker,"text",matrix1,stickerView)
            dialog.dismiss()
        }
        dialog.show()
    }
    private fun testAdd(
        sticker1: TextSticker,
        text: String,
        matrix: Matrix,
        stickerView: StickerView
    ) {
        sticker1!!.text = text
        sticker1!!.setTextColor(Color.BLUE)
        sticker1!!.setTextAlign(Layout.Alignment.ALIGN_CENTER)
        sticker1!!.resizeText()
        sticker1!!.matrix=matrix
//        stickerView!!.addSticker(sticker1!!,Sticker.Position.CENTER,"")
                    stickerView!!.replace(sticker1, true)

    }
}