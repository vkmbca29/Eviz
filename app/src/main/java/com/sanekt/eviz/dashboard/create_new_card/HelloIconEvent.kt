package com.sanekt.eviz.dashboard.create_new_card

import android.view.MotionEvent
import android.widget.Toast
import com.sanekt.eviz.dashboard.create_new_card.StickerIconEvent
import com.sanekt.eviz.dashboard.create_new_card.StickerView


/**
 * @author wupanjie
 * @see StickerIconEvent
 */
class HelloIconEvent : StickerIconEvent {
    override fun onActionDown(stickerView: StickerView?, event: MotionEvent?) {}
    override fun onActionMove(stickerView: StickerView?, event: MotionEvent?) {}
    override fun onActionUp(stickerView: StickerView?, event: MotionEvent?) {
        Toast.makeText(stickerView?.context, "Hello World!", Toast.LENGTH_SHORT).show()
    }
}