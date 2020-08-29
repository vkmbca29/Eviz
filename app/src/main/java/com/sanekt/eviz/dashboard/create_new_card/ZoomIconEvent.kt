package com.sanekt.eviz.dashboard.create_new_card

import android.view.MotionEvent

/**
 * @author wupanjie
 */
class ZoomIconEvent : StickerIconEvent {
    override fun onActionDown(stickerView: StickerView?, event: MotionEvent?) {}
    override fun onActionMove(stickerView: StickerView?, event: MotionEvent?) {
        event?.let { stickerView?.zoomAndRotateCurrentSticker(it) }
    }

    override fun onActionUp(stickerView: StickerView?, event: MotionEvent?) {
        if (stickerView?.onStickerOperationListener != null) {
            stickerView.onStickerOperationListener!!
                    .onStickerZoomFinished(stickerView.currentSticker!!)
        }
    }
}