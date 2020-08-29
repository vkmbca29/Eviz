package com.sanekt.eviz.dashboard.create_new_card

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import androidx.annotation.IntDef
import android.view.MotionEvent
import com.sanekt.eviz.MainActivity
import com.sanekt.eviz.R
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * @author wupanjie
 */
class BitmapStickerIcon(
    drawable: Drawable?, @Gravity gravity: Int,
    mainActivity: MainActivity
) : DrawableSticker(
    drawable!!,
    mainActivity
, "sticker", ""
), StickerIconEvent {
    @IntDef(LEFT_TOP, RIGHT_TOP, LEFT_BOTTOM, RIGHT_BOTOM)
    @Retention(RetentionPolicy.SOURCE)
    annotation class Gravity

    var iconRadius = DEFAULT_ICON_RADIUS
    var iconExtraRadius = DEFAULT_ICON_EXTRA_RADIUS
    var x = 0f
    var y = 0f
//    @get:Gravity
//    @Gravity
    var position = LEFT_TOP
    var iconEvent: StickerIconEvent? = null
    fun draw(canvas: Canvas, paint: Paint?) {
        paint?.setColor(context.resources.getColor(R.color.border))
        paint?.let { canvas.drawCircle(x, y, iconRadius, it) }
        super.draw(canvas)
    }

    override fun onActionDown(stickerView: StickerView?, event: MotionEvent?) {
        if (iconEvent != null) {
            iconEvent!!.onActionDown(stickerView, event)
        }
    }

    override fun onActionMove(stickerView: StickerView?, event: MotionEvent?) {
        if (iconEvent != null) {
            iconEvent!!.onActionMove(stickerView, event)
        }
    }

    override fun onActionUp(stickerView: StickerView?, event: MotionEvent?) {
        if (iconEvent != null) {
            iconEvent!!.onActionUp(stickerView, event)
        }
    }

    companion object {
        const val DEFAULT_ICON_RADIUS = 30f
        const val DEFAULT_ICON_EXTRA_RADIUS = 10f
        const val LEFT_TOP = 0
        const val RIGHT_TOP = 1
        const val LEFT_BOTTOM = 2
        const val RIGHT_BOTOM = 3
    }

    init {
        position = gravity
    }
}