package com.sanekt.eviz.dashboard.create_new_card

/**
 * @author wupanjie
 */
class FlipHorizontallyEvent : AbstractFlipEvent() {
    @get:StickerView.Flip
    override val flipDirection: Int
        protected get() = StickerView.FLIP_HORIZONTALLY
}