package com.sanekt.eviz.dashboard.create_new_card

import android.view.MotionEvent

abstract class AbstractFlipEvent : StickerIconEvent {
    override fun onActionDown(stickerView: StickerView?, event: MotionEvent?) {}
    override fun onActionMove(stickerView: StickerView?, event: MotionEvent?) {}
    override fun onActionUp(stickerView: StickerView?, event: MotionEvent?) {
        stickerView?.flipCurrentSticker(flipDirection)
    }

    @get:StickerView.Flip
    protected abstract val flipDirection: Int
}