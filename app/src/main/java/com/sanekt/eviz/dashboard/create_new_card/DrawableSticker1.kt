package com.sanekt.eviz.dashboard.create_new_card

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import androidx.annotation.IntRange
import java.io.File
import java.io.IOException


/**
 * @author wupanjie
 */
open class DrawableSticker1(
//    private var drawable: Drawable,
    mainActivity: Context,
    s: String,
    uri: String
) : Sticker() {
    private val realBounds: Rect
    var context: Context = mainActivity
    var from = s
    var bitmap: Bitmap? = null
    var uri: String? = uri

//    override fun getDrawable(): Drawable {
//        return drawable
//    }
//
//    override fun setDrawable(drawable: Drawable): DrawableSticker1 {
////        this.drawable = drawable
//        return this
//    }

    override fun draw(canvas: Canvas) {
        canvas.save()
        canvas.concat(matrix)
        if (TextUtils.isEmpty(from)) {
            var uri =Uri.parse(uri)
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                bitmap=getResizedBitmap(bitmap,100,100)

            } catch (e: IOException) {
                e.printStackTrace()
            }
            val paint = Paint()
            paint.isAntiAlias = true
            paint.isFilterBitmap = true
            paint.isDither = true
            if (bitmap != null) {
                canvas.drawBitmap(bitmap!!, Matrix(), null)
            }
        } else if(from.equals("uri")){
            var uri:Uri =Uri.parse(uri)
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                bitmap=getResizedBitmap(bitmap,100,100)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val paint = Paint()
            paint.isAntiAlias = true
            paint.isFilterBitmap = true
            paint.isDither = true
            if (bitmap != null) {
                canvas.drawBitmap(bitmap!!, Matrix(), null)
            }
        }/*else{
            drawable.bounds = realBounds
            drawable.draw(canvas)
        }*/

        canvas.restore()
    }
    open fun getResizedBitmap(bm: Bitmap?, newWidth: Int, newHeight: Int): Bitmap? {
        val width = bm?.width
        val height = bm?.height
        val scaleWidth = newWidth.toFloat() / width!!
        val scaleHeight = newHeight.toFloat() / height!!
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)
        // "RECREATE" THE NEW BITMAP
        val resizedBitmap = Bitmap.createBitmap(
            bm, 0, 0, width, height, matrix, false
        )
        bm.recycle()
        return resizedBitmap
    }
    override fun setAlpha(@IntRange(from = 0, to = 255) alpha: Int): DrawableSticker1 {
//        drawable.alpha = alpha
        return this
    }

    override fun getWidth(): Int {
        return if(TextUtils.isEmpty(from) && bitmap!=null || from.equals("uri") || from.equals("logo")){
            100
        }else
            100
    }

    override fun getHeight(): Int {
        return if(TextUtils.isEmpty(from) && bitmap!=null || from.equals("uri") || from.equals("logo")){
            100
        }else
            100
    }

    override fun release() {
        super.release()
//        if (drawable != null) {
////            drawable = null
//        }
    }

    init {
        realBounds = Rect(0, 0, getWidth(), getHeight())
    }
}