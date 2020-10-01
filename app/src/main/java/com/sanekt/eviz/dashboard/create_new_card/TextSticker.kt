package com.sanekt.eviz.dashboard.create_new_card

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.sanekt.eviz.R
import java.lang.Exception

/**
 * Customize your sticker with text and image background.
 * You can place some text into a given region, however,
 * you can also add a plain text sticker. To support text
 * auto resizing , I take most of the code from AutoResizeTextView.
 * See https://adilatwork.blogspot.com/2014/08/android-textview-which-resizes-its-text.html
 * Notice: It's not efficient to add long text due to too much of
 * StaticLayout object allocation.
 * Created by liutao on 30/11/2016.
 */
open class TextSticker @JvmOverloads constructor(private val context: Context, drawable: Drawable? = null) : Sticker() {
    private val realBounds: Rect
    private val textRect: Rect
    private val textPaint: TextPaint
    private var drawable: Drawable?
    private var staticLayout: StaticLayout? = null
    private var alignment: Layout.Alignment
    private var textColor: Int = 0
    private var typeface: Int = 0
    var text: String? = null
        set
    var text1: String? = null
        set
    /**
     * Upper bounds for text size.
     * This acts as a starting point for resizing.
     */
    private var maxTextSizePixels: Float
    /**
     * @return lower text size limit, in pixels.
     */
    /**
     * Lower bounds for text size.
     */
    var minTextSizePixels: Float
        private set
    /**
     * Line spacing multiplier.
     */
    private var lineSpacingMultiplier = 1.0f
    /**
     * Additional line spacing.
     */
    private var lineSpacingExtra = 0.0f

    override fun draw(canvas: Canvas) {
        val matrix = matrix
        canvas.save()
        canvas.concat(matrix)
        if (drawable != null) {
            drawable!!.bounds = realBounds
            drawable!!.draw(canvas)
        }
        canvas.restore()
        canvas.save()
        canvas.concat(matrix)
        if (textRect.width() == getWidth()) {
            try {
                val dy = getHeight() / 2 - staticLayout!!.getHeight() / 2
                // center vertical
                canvas.translate(0f, dy.toFloat())
            }catch (e:Exception){
                e.printStackTrace()
            }
        } else {
            val dx = textRect.left
            val dy = textRect.top + textRect.height() / 2 - staticLayout!!.getHeight() / 2
            canvas.translate(dx.toFloat(), dy.toFloat())
        }
        try {
            staticLayout!!.draw(canvas)
        }catch (e:Exception){

        }
        canvas.restore()
    }

    override fun getWidth(): Int {
        return drawable!!.intrinsicWidth
    }

    override fun getHeight(): Int {
        return drawable!!.intrinsicHeight
    }

    override fun release() {
        super.release()
        if (drawable != null) {
            drawable = null
        }
    }

    override fun setAlpha(@IntRange(from = 0, to = 255) alpha: Int): TextSticker {
        textPaint.alpha = alpha
        return this
    }

//    override fun getDrawable(): Drawable {
//        return drawable!!
//    }
//
//    override fun setDrawable(drawable: Drawable): TextSticker {
//        this.drawable = drawable
//        realBounds[0, 0, getWidth()] = getHeight()
//        textRect[0, 0, getWidth()] = getHeight()
//        return this
//    }

    fun setDrawable(drawable: Drawable, region: Rect?): TextSticker {
        this.drawable = drawable
        realBounds[0, 0, getWidth()] = getHeight()
        if (region == null) {
            textRect[0, 0, getWidth()] = getHeight()
        } else {
            textRect[region.left, region.top, region.right] = region.bottom
        }
        return this
    }

    fun setTypeface(typeface: Typeface?): TextSticker {
        textPaint.typeface = typeface
        return this
    }
    fun setTypefaceNo(typeface1: Int?): TextSticker {
        if (typeface1 != null) {
            typeface = typeface1
        }
        return this
    }

    fun setTextColor(@ColorInt color: Int): TextSticker {
        textPaint.color = color
        textColor=color
        return this
    }
    fun getTextColor(): Int {
        textColor
        return textPaint.color
    }
    fun getTypeFace(): Typeface? {
        return textPaint.typeface
    }
    fun getTypeFaceNo(): Int? {
        return typeface
    }

    fun setTextAlign(alignment: Layout.Alignment): TextSticker {
        this.alignment = alignment
        return this
    }
    fun setTextColorCode(color: Int): TextSticker {
        this.textColor = color
        return this
    }
    fun getTextColorCode(): Int {
        return textColor
    }
    fun setMaxTextSize(@Dimension(unit = Dimension.SP) size: Float): TextSticker {
        textPaint.textSize = convertSpToPx(size)
        maxTextSizePixels = textPaint.textSize
        return this
    }

    /**
     * Sets the lower text size limit
     *
     * @param minTextSizeScaledPixels the minimum size to use for text in this view,
     * in scaled pixels.
     */
    fun setMinTextSize(minTextSizeScaledPixels: Float): TextSticker {
        minTextSizePixels = convertSpToPx(minTextSizeScaledPixels)
        return this
    }

    fun setLineSpacing(add: Float, multiplier: Float): TextSticker {
        lineSpacingMultiplier = multiplier
        lineSpacingExtra = add
        return this
    }

    fun setText(text: String?): TextSticker {
        this.text = text
        this.text1 = text
        return this
    }

    /**
     * Resize this view's text size with respect to its getWidth() and getHeight()
     * (minus padding). You should always call this method after the initialization.
     */
    fun resizeText(): TextSticker {
        val availableHeightPixels = textRect.height()
        val availableWidthPixels = textRect.width()
        val text: CharSequence? = text
        // Safety check
// (Do not resize if the view does not have dimensions or if there is no text)
        if (text == null || text.length <= 0 || availableHeightPixels <= 0 || availableWidthPixels <= 0 || maxTextSizePixels <= 0) {
            return this
        }
        var targetTextSizePixels = maxTextSizePixels
        var targetTextHeightPixels = getTextHeightPixels(text, availableWidthPixels, targetTextSizePixels)
        // Until we either fit within our TextView
// or we have reached our minimum text size,
// incrementally try smaller sizes
        while (targetTextHeightPixels > availableHeightPixels
                && targetTextSizePixels > minTextSizePixels) {
            targetTextSizePixels = Math.max(targetTextSizePixels - 2, minTextSizePixels)
            targetTextHeightPixels = getTextHeightPixels(text, availableWidthPixels, targetTextSizePixels)
        }
        // If we have reached our minimum text size and the text still doesn't fit,
// append an ellipsis
// (NOTE: Auto-ellipsize doesn't work hence why we have to do it here)
        if (targetTextSizePixels == minTextSizePixels
                && targetTextHeightPixels > availableHeightPixels) { // Make a copy of the original TextPaint object for measuring
            val textPaintCopy = TextPaint(textPaint)
            textPaintCopy.textSize = targetTextSizePixels
            // Measure using a StaticLayout instance
            val staticLayout = StaticLayout(text, textPaintCopy, availableWidthPixels, Layout.Alignment.ALIGN_NORMAL,
                    lineSpacingMultiplier, lineSpacingExtra, false)
            // Check that we have a least one line of rendered text
            if (staticLayout.lineCount > 0) { // Since the line at the specific vertical position would be cut off,
// we must trim up to the previous line and add an ellipsis
                val lastLine = staticLayout.getLineForVertical(availableHeightPixels) - 1
                if (lastLine >= 0) {
                    val startOffset = staticLayout.getLineStart(lastLine)
                    var endOffset = staticLayout.getLineEnd(lastLine)
                    var lineWidthPixels = staticLayout.getLineWidth(lastLine)
                    val ellipseWidth = textPaintCopy.measureText(mEllipsis)
                    // Trim characters off until we have enough room to draw the ellipsis
                    while (availableWidthPixels < lineWidthPixels + ellipseWidth) {
                        endOffset--
                        lineWidthPixels = textPaintCopy.measureText(text.subSequence(startOffset, endOffset + 1).toString())
                    }
                    setText(text.subSequence(0, endOffset).toString() + mEllipsis)
                }
            }
        }
        textPaint.textSize = targetTextSizePixels
        staticLayout = StaticLayout(this.text, textPaint, textRect.width(), alignment, lineSpacingMultiplier,
                lineSpacingExtra, true)
        return this
    }

    /**
     * Sets the text size of a clone of the view's [TextPaint] object
     * and uses a [StaticLayout] instance to measure the getHeight() of the text.
     *
     * @return the getHeight() of the text when placed in a view
     * with the specified getWidth()
     * and when the text has the specified size.
     */
    protected fun getTextHeightPixels(source: CharSequence, availableWidthPixels: Int,
                                      textSizePixels: Float): Int {
        textPaint.textSize = textSizePixels
        // It's not efficient to create a StaticLayout instance
// every time when measuring, we can use StaticLayout.Builder
// since api 23.
        val staticLayout = StaticLayout(source, textPaint, availableWidthPixels, Layout.Alignment.ALIGN_NORMAL,
                lineSpacingMultiplier, lineSpacingExtra, true)
        return staticLayout.getHeight()
    }

    /**
     * @return the number of pixels which scaledPixels corresponds to on the device.
     */
    private fun convertSpToPx(scaledPixels: Float): Float {
        return scaledPixels * context.resources.displayMetrics.scaledDensity
    }
    private fun setTypeFace(){
        textPaint.typeface= Typeface.DEFAULT_BOLD
    }

    companion object {
        /**
         * Our ellipsis string.
         */
        private const val mEllipsis = "\u2026"
    }

    init {
        this.drawable = context.resources.getDrawable(R.drawable.sticker_ic_close_white_18dp)
        if (drawable == null) {
            this.drawable = ContextCompat.getDrawable(context, R.drawable.sticker_transparent_background)
        }
        textPaint = TextPaint(TextPaint.ANTI_ALIAS_FLAG)
        realBounds = Rect(0, 0, getWidth(), getHeight())
        textRect = Rect(0, 0, getWidth(), getHeight())
        minTextSizePixels = convertSpToPx(6f)
        maxTextSizePixels = convertSpToPx(24f)
        alignment = Layout.Alignment.ALIGN_CENTER
        textPaint.textSize = maxTextSizePixels
    }
}