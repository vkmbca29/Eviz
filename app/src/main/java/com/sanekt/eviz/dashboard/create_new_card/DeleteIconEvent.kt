package com.sanekt.eviz.dashboard.create_new_card

import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import com.sanekt.eviz.MainActivity
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
//        stickerView?.removeCurrentSticker()
//        val intent = Intent(context, CropActivity::class.java)
//        context.startActivity(intent)
//        var drawable = ContextCompat.getDrawable(context, R.drawable.haizewang_23);
//        drawable?.let { DrawableSticker(drawable,context,"") }?.let {
//            stickerView!!.replace(it, true)
//        }
        if (stickerView?.currentSticker is TextSticker) {
            var sticker=stickerView.currentSticker
            openColorPickerDialog(sticker,stickerView)
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
                Toast.makeText(context, "hello"+selectedColor, Toast.LENGTH_SHORT).show()
                if (sticker is TextSticker) {
                    sticker.setTextColor(selectedColor)
                    stickerView!!.replace(sticker)
                    stickerView!!.invalidate()
                }
            }
            .setPositiveButton(
                "ok"
            ) { dialog, selectedColor, allColors ->
                Toast.makeText(context, "hello"+selectedColor, Toast.LENGTH_SHORT).show()
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
//        ImagePicker.with(context)
//            // Crop Square image
//            .crop()
//            .setImageProviderInterceptor { imageProvider ->
//                // Intercept ImageProvider
//                Log.d("ImagePicker", "Selected ImageProvider: " + imageProvider.name)
//            }
//            // Image resolution will be less than 512 x 512
//            .maxResultSize(512, 512)
//            .start(Companion.PROFILE_IMAGE_REQ_CODE)

        ImagePicker.with(context)
            // Crop Image(User can choose Aspect Ratio)
            .crop()
            // User can only select image from Gallery
            .galleryOnly()

            .galleryMimeTypes(  //no gif images at all
                mimeTypes = arrayOf(
                    "image/png",
                    "image/jpg",
                    "image/jpeg"
                )
            )
            // Image resolution will be less than 1080 x 1920
            .maxResultSize(1080, 1920)
            .start(PROFILE_IMAGE_REQ_CODE)
    }

    companion object {
        private const val PROFILE_IMAGE_REQ_CODE = 101
    }
}