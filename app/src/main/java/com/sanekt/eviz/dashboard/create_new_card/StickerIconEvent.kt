package com.sanekt.eviz.dashboard.create_new_card

import android.view.MotionEvent

/**
 * @author wupanjie
 */
open interface StickerIconEvent {
    fun onActionDown(stickerView: StickerView?, event: MotionEvent?)
    fun onActionMove(stickerView: StickerView?, event: MotionEvent?)
    fun onActionUp(stickerView: StickerView?, event: MotionEvent?)
}