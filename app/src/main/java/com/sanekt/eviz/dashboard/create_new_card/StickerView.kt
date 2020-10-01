package com.sanekt.eviz.dashboard.create_new_card

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.os.SystemClock
import androidx.annotation.IntDef
import androidx.core.view.MotionEventCompat
import androidx.core.view.ViewCompat
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.FrameLayout
import com.sanekt.eviz.R
import java.io.File
import java.lang.Exception
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.util.*
import kotlin.jvm.Throws

/**
 * Sticker View
 * @author wupanjie
 */
class StickerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private var showIcons = false
    private var showBorder = false
    private var bringToFrontCurrentSticker = false

//    @IntDef(ActionMode.NONE.toLong(), ActionMode.DRAG.toLong(), ActionMode.ZOOM_WITH_TWO_FINGER.toLong(), ActionMode.ICON.toLong(), ActionMode.CLICK.toLong())
    @Retention(RetentionPolicy.SOURCE)
    protected annotation class ActionMode {
        companion object {
            var NONE = 0
            var DRAG = 1
            var ZOOM_WITH_TWO_FINGER = 2
            var ICON = 3
            var CLICK = 4
        }
    }

    @IntDef(flag = true, value = [FLIP_HORIZONTALLY, FLIP_VERTICALLY])
    @Retention(RetentionPolicy.SOURCE)
    annotation class Flip

    private val stickers: MutableList<Sticker> = ArrayList()
    var icons: MutableList<BitmapStickerIcon> = ArrayList(4)
    private val borderPaint = Paint()
    private val stickerRect = RectF()
    private val sizeMatrix = Matrix()
    private val downMatrix = Matrix()
    private val moveMatrix = Matrix()
    // region storing variables
    private val bitmapPoints = FloatArray(8)
    private val bounds = FloatArray(8)
    private val point = FloatArray(2)
    private val currentCenterPoint = PointF()
    private val tmp = FloatArray(2)
    private var midPoint = PointF()
    // endregion
    private val touchSlop: Int
    private var currentIcon: BitmapStickerIcon? = null
    //the first point down position
    private var downX = 0f
    private var downY = 0f
    private var oldDistance = 0f
    private var oldRotation = 0f
    @ActionMode
    private var currentMode = ActionMode.NONE
    var currentSticker: Sticker? = null
        private set
    var isLocked = false
        set
    var isConstrained = false
        set
    var onStickerOperationListener: OnStickerOperationListener? = null
        set
    private var lastClickTime: Long = 0
    var minClickDelayTime = DEFAULT_MIN_CLICK_DELAY_TIME
        private set


    var dataType=""
    fun configDefaultIcons() {
//        val deleteIcon = BitmapStickerIcon(
//            ContextCompat.getDrawable(context, R.drawable.sticker_ic_close_white_18dp),
//            BitmapStickerIcon.LEFT_TOP,
//            this
//        )
//        deleteIcon.iconEvent = DeleteIconEvent(MainActivity())
//        val zoomIcon = BitmapStickerIcon(
//            ContextCompat.getDrawable(context, R.drawable.sticker_ic_scale_white_18dp),
//            BitmapStickerIcon.RIGHT_BOTOM,
//            this
//        )
//        zoomIcon.iconEvent = ZoomIconEvent()
//        val flipIcon = BitmapStickerIcon(
//            ContextCompat.getDrawable(context, R.drawable.sticker_ic_flip_white_18dp),
//            BitmapStickerIcon.RIGHT_TOP,
//            this
//        )
//        flipIcon.iconEvent = FlipHorizontallyEvent()
//        icons.clear()
//        icons.add(deleteIcon)
//        icons.add(zoomIcon)
//        icons.add(flipIcon)
    }

    /**
     * Swaps sticker at layer [[oldPos]] with the one at layer [[newPos]].
     * Does nothing if either of the specified layers doesn't exist.
     */
    fun swapLayers(oldPos: Int, newPos: Int) {
        if (stickers.size >= oldPos && stickers.size >= newPos) {
            Collections.swap(stickers, oldPos, newPos)
            invalidate()
        }
    }

    /**
     * Sends sticker from layer [[oldPos]] to layer [[newPos]].
     * Does nothing if either of the specified layers doesn't exist.
     */
    fun sendToLayer(oldPos: Int, newPos: Int) {
        if (stickers.size >= oldPos && stickers.size >= newPos) {
            val s = stickers[oldPos]
            stickers.removeAt(oldPos)
            stickers.add(newPos, s)
            invalidate()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            stickerRect.left = left.toFloat()
            stickerRect.top = top.toFloat()
            stickerRect.right = right.toFloat()
            stickerRect.bottom = bottom.toFloat()
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        drawStickers(canvas)
    }

    protected fun drawStickers(canvas: Canvas) {
        for (i in stickers.indices) {
            val sticker = stickers[i]
            try {
                sticker?.draw(canvas)
            }catch (e:Exception){

            }
        }
        if (currentSticker != null && !isLocked && (showBorder || showIcons)) {
            getStickerPoints(currentSticker, bitmapPoints)
            val x1 = bitmapPoints[0]
            val y1 = bitmapPoints[1]
            val x2 = bitmapPoints[2]
            val y2 = bitmapPoints[3]
            val x3 = bitmapPoints[4]
            val y3 = bitmapPoints[5]
            val x4 = bitmapPoints[6]
            val y4 = bitmapPoints[7]
            if (showBorder) {
                canvas.drawLine(x1, y1, x2, y2, borderPaint)
                canvas.drawLine(x1, y1, x3, y3, borderPaint)
                canvas.drawLine(x2, y2, x4, y4, borderPaint)
                canvas.drawLine(x4, y4, x3, y3, borderPaint)
            }
            //draw icons
            if (showIcons) {
                val rotation = calculateRotation(x4, y4, x3, y3)
                for (i in icons.indices) {
                    val icon = icons[i]
                    when (icon.position) {
                        BitmapStickerIcon.LEFT_TOP -> configIconMatrix(icon, x1, y1, rotation)
                        BitmapStickerIcon.RIGHT_TOP -> configIconMatrix(icon, x2, y2, rotation)
                        BitmapStickerIcon.LEFT_BOTTOM -> configIconMatrix(icon, x3, y3, rotation)
                        BitmapStickerIcon.RIGHT_BOTOM -> configIconMatrix(icon, x4, y4, rotation)
                    }
                    icon.draw(canvas, borderPaint)
                }
            }
        }
    }

    protected fun configIconMatrix(icon: BitmapStickerIcon, x: Float, y: Float,
                                   rotation: Float) {
        icon.x = x
        icon.y = y
        icon.matrix.reset()
        icon.matrix.postRotate(rotation, icon.getWidth() / 2.toFloat(), icon.getHeight() / 2.toFloat())
        icon.matrix.postTranslate(x - icon.getWidth() / 2, y - icon.getHeight() / 2)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (isLocked) return super.onInterceptTouchEvent(ev)
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x
                downY = ev.y
                return findCurrentIconTouched() != null || findHandlingSticker() != null
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isLocked) {
            return super.onTouchEvent(event)
        }
        val action = MotionEventCompat.getActionMasked(event)
        when (action) {
            MotionEvent.ACTION_DOWN -> if (!onTouchDown(event)) {
                return false
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDistance = calculateDistance(event)
                oldRotation = calculateRotation(event)
                midPoint = calculateMidPoint(event)
                if (currentSticker != null && isInStickerArea(currentSticker!!, event.getX(1),
                                event.getY(1)) && findCurrentIconTouched() == null) {
                    currentMode = ActionMode.ZOOM_WITH_TWO_FINGER
                }
            }
            MotionEvent.ACTION_MOVE -> {
                handleCurrentMode(event)
                invalidate()
            }
            MotionEvent.ACTION_UP -> onTouchUp(event)
            MotionEvent.ACTION_POINTER_UP -> {
                if (currentMode == ActionMode.ZOOM_WITH_TWO_FINGER && currentSticker != null) {
                    if (onStickerOperationListener != null) {
                        onStickerOperationListener!!.onStickerZoomFinished(currentSticker!!)
                    }
                }
                currentMode = ActionMode.NONE
            }
        }
        return true
    }

    /**
     * @param event MotionEvent received from [)][.onTouchEvent]
     */
    protected fun onTouchDown(event: MotionEvent): Boolean {
        currentMode = ActionMode.DRAG
        downX = event.x
        downY = event.y
        midPoint = calculateMidPoint()
        oldDistance = calculateDistance(midPoint.x, midPoint.y, downX, downY)
        oldRotation = calculateRotation(midPoint.x, midPoint.y, downX, downY)
        currentIcon = findCurrentIconTouched()
        if (currentIcon != null) {
            currentMode = ActionMode.ICON
            currentIcon!!.onActionDown(this, event)
        } else {
            currentSticker = findHandlingSticker()
        }
        if (currentSticker != null) {
            downMatrix.set(currentSticker!!.matrix)
            if (bringToFrontCurrentSticker) {
                stickers.remove(currentSticker!!)
                stickers.add(currentSticker!!)
            }
            if (onStickerOperationListener != null) {
                onStickerOperationListener!!.onStickerTouchedDown(currentSticker!!)
            }
        }
        if (currentIcon == null && currentSticker == null) {
            return false
        }
        invalidate()
        return true
    }

    protected fun onTouchUp(event: MotionEvent) {
        val currentTime = SystemClock.uptimeMillis()
        if (currentMode == ActionMode.ICON && currentIcon != null && currentSticker != null) {
            currentIcon!!.onActionUp(this, event)
        }
        if (currentMode == ActionMode.DRAG && Math.abs(event.x - downX) < touchSlop && Math.abs(event.y - downY) < touchSlop && currentSticker != null) {
            currentMode = ActionMode.CLICK
            if (onStickerOperationListener != null) {
                onStickerOperationListener!!.onStickerClicked(currentSticker!!)
            }
            if (currentTime - lastClickTime < minClickDelayTime) {
                if (onStickerOperationListener != null) {
                    onStickerOperationListener!!.onStickerDoubleTapped(currentSticker!!)
                }
            }
        }
        if (currentMode == ActionMode.DRAG && currentSticker != null) {
            if (onStickerOperationListener != null) {
                onStickerOperationListener!!.onStickerDragFinished(currentSticker!!)
            }
        }
        currentMode = ActionMode.NONE
        lastClickTime = currentTime
    }

    protected fun handleCurrentMode(event: MotionEvent) {
        when (currentMode) {
            ActionMode.NONE, ActionMode.CLICK -> {
            }
            ActionMode.DRAG -> if (currentSticker != null) {
                moveMatrix.set(downMatrix)
                moveMatrix.postTranslate(event.x - downX, event.y - downY)
                currentSticker!!.setMatrix(moveMatrix)
                if (isConstrained) {
                    constrainSticker(currentSticker!!)
                }
            }
            ActionMode.ZOOM_WITH_TWO_FINGER -> if (currentSticker != null) {
                val newDistance = calculateDistance(event)
                val newRotation = calculateRotation(event)
                moveMatrix.set(downMatrix)
                moveMatrix.postScale(newDistance / oldDistance, newDistance / oldDistance, midPoint.x,
                        midPoint.y)
                moveMatrix.postRotate(newRotation - oldRotation, midPoint.x, midPoint.y)
                currentSticker!!.setMatrix(moveMatrix)
            }
            ActionMode.ICON -> if (currentSticker != null && currentIcon != null) {
                currentIcon!!.onActionMove(this, event)
            }
        }
    }

    fun zoomAndRotateCurrentSticker(event: MotionEvent) {
        zoomAndRotateSticker(currentSticker, event)
    }

    fun zoomAndRotateSticker(sticker: Sticker?, event: MotionEvent) {
        if (sticker != null) {
            val newDistance = calculateDistance(midPoint.x, midPoint.y, event.x, event.y)
            val newRotation = calculateRotation(midPoint.x, midPoint.y, event.x, event.y)
            moveMatrix.set(downMatrix)
            moveMatrix.postScale(newDistance / oldDistance, newDistance / oldDistance, midPoint.x,
                    midPoint.y)
            moveMatrix.postRotate(newRotation - oldRotation, midPoint.x, midPoint.y)
            currentSticker!!.setMatrix(moveMatrix)
        }
    }

    protected fun constrainSticker(sticker: Sticker) {
        var moveX = 0f
        var moveY = 0f
        val width = width
        val height = height
        sticker.getMappedCenterPoint(currentCenterPoint, point, tmp)
        if (currentCenterPoint.x < 0) {
            moveX = -currentCenterPoint.x
        }
        if (currentCenterPoint.x > width) {
            moveX = width - currentCenterPoint.x
        }
        if (currentCenterPoint.y < 0) {
            moveY = -currentCenterPoint.y
        }
        if (currentCenterPoint.y > height) {
            moveY = height - currentCenterPoint.y
        }
        sticker.matrix.postTranslate(moveX, moveY)
    }

    protected fun findCurrentIconTouched(): BitmapStickerIcon? {
        for (icon in icons) {
            val x = icon.x - downX
            val y = icon.y - downY
            val distance_pow_2 = x * x + y * y
            if (distance_pow_2 <= Math.pow(icon.iconRadius + icon.iconRadius.toDouble(), 2.0)) {
                return icon
            }
        }
        return null
    }

    /**
     * find the touched Sticker
     */
    protected fun findHandlingSticker(): Sticker? {
        for (i in stickers.indices.reversed()) {
            if (isInStickerArea(stickers[i], downX, downY)) {
                return stickers[i]
            }
        }
        return null
    }

    protected fun isInStickerArea(sticker: Sticker, downX: Float, downY: Float): Boolean {
        tmp[0] = downX
        tmp[1] = downY
        return sticker.contains(tmp)
    }

    protected fun calculateMidPoint(event: MotionEvent?): PointF {
        if (event == null || event.pointerCount < 2) {
            midPoint[0f] = 0f
            return midPoint
        }
        val x = (event.getX(0) + event.getX(1)) / 2
        val y = (event.getY(0) + event.getY(1)) / 2
        midPoint[x] = y
        return midPoint
    }

    protected fun calculateMidPoint(): PointF {
        if (currentSticker == null) {
            midPoint[0f] = 0f
            return midPoint
        }
        currentSticker!!.getMappedCenterPoint(midPoint, point, tmp)
        return midPoint
    }

    /**
     * calculate rotation in line with two fingers and x-axis
     */
    protected fun calculateRotation(event: MotionEvent?): Float {
        return if (event == null || event.pointerCount < 2) {
            0f
        } else calculateRotation(event.getX(0), event.getY(0), event.getX(1), event.getY(1))
    }

    protected fun calculateRotation(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val x = x1 - x2.toDouble()
        val y = y1 - y2.toDouble()
        val radians = Math.atan2(y, x)
        return Math.toDegrees(radians).toFloat()
    }

    /**
     * calculate Distance in two fingers
     */
    protected fun calculateDistance(event: MotionEvent?): Float {
        return if (event == null || event.pointerCount < 2) {
            0f
        } else calculateDistance(event.getX(0), event.getY(0), event.getX(1), event.getY(1))
    }

    protected fun calculateDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val x = x1 - x2.toDouble()
        val y = y1 - y2.toDouble()
        return Math.sqrt(x * x + y * y).toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)
        for (i in stickers.indices) {
            val sticker = stickers[i]
            sticker?.let { transformSticker(it) }
        }
    }

    /**
     * Sticker's getDrawable() will be too bigger or smaller
     * This method is to transform it to fit
     * step 1：let the center of the sticker image is coincident with the center of the View.
     * step 2：Calculate the zoom and zoom
     */
    protected fun transformSticker(sticker: Sticker?) {
        if (sticker == null) {
            Log.e(TAG, "transformSticker: the bitmapSticker is null or the bitmapSticker bitmap is null")
            return
        }
        sizeMatrix.reset()
        val width = width.toFloat()
        val height = height.toFloat()
        val stickerWidth = sticker.getWidth().toFloat()
        val stickerHeight = sticker.getHeight().toFloat()
        //step 1
        val offsetX = (width - stickerWidth) / 2
        val offsetY = (height - stickerHeight) / 2
        sizeMatrix.postTranslate(offsetX, offsetY)
        //step 2
        val scaleFactor: Float
        scaleFactor = if (width < height) {
            width / stickerWidth
        } else {
            height / stickerHeight
        }
        sizeMatrix.postScale(scaleFactor / 2f, scaleFactor / 2f, width / 2f, height / 2f)
        sticker.matrix.reset()
        sticker.setMatrix(sizeMatrix)
        invalidate()
    }

    fun flipCurrentSticker(direction: Int) {
        flip(currentSticker, direction)
    }

    fun flip(sticker: Sticker?, @Flip direction: Int) {
        if (sticker != null) {
            sticker.getCenterPoint(midPoint)
            if (direction and FLIP_HORIZONTALLY > 0) {
                sticker.matrix.preScale(-1f, 1f, midPoint.x, midPoint.y)
                sticker.isFlippedHorizontally = !sticker.isFlippedHorizontally
            }
            if (direction and FLIP_VERTICALLY > 0) {
                sticker.matrix.preScale(1f, -1f, midPoint.x, midPoint.y)
                sticker.isFlippedVertically = !sticker.isFlippedVertically
            }
            if (onStickerOperationListener != null) {
                onStickerOperationListener!!.onStickerFlipped(sticker)
            }
            invalidate()
        }
    }

    @JvmOverloads
    fun replace(sticker: Sticker?, needStayState: Boolean = true): Boolean {
        return if (currentSticker != null && sticker != null) {
            val width = width.toFloat()
            val height = height.toFloat()
            if (needStayState) {
                sticker.setMatrix(currentSticker!!.matrix)
                sticker.isFlippedVertically = currentSticker!!.isFlippedVertically
                sticker.isFlippedHorizontally = currentSticker!!.isFlippedHorizontally
            } else {
                currentSticker!!.matrix.reset()
                // reset scale, angle, and put it in center
                val offsetX = (width - currentSticker!!.getWidth()) / 2f
                val offsetY = (height - currentSticker!!.getHeight()) / 2f
                sticker.matrix.postTranslate(offsetX, offsetY)
                val scaleFactor: Float = if (width < height) {
                    width / 100
                } else {
                    height / 100
                }
                sticker.matrix.postScale(scaleFactor / 2f, scaleFactor / 2f, width / 2f, height / 2f)
            }
            val index = stickers.indexOf(currentSticker!!)
            stickers[index] = sticker
            currentSticker = sticker
            invalidate()
            true
        } else {
            false
        }
    }

    fun remove(sticker: Sticker?): Boolean {
        return if (stickers.contains(sticker)) {
            stickers.remove(sticker)
            if (onStickerOperationListener != null) {
                onStickerOperationListener!!.onStickerDeleted(sticker!!)
            }
            if (currentSticker === sticker) {
                currentSticker = null
            }
            invalidate()
            true
        } else {
            Log.d(TAG, "remove: the sticker is not in this StickerView")
            false
        }
    }

    fun removeCurrentSticker(): Boolean {
        return remove(currentSticker)
    }

    fun removeAllStickers() {
        stickers.clear()
        if (currentSticker != null) {
            currentSticker!!.release()
            currentSticker = null
        }
        invalidate()
    }

//    fun addSticker(sticker: Sticker): StickerView {
//        return addSticker(sticker, Sticker.Position.LEFT,"")
//    }

    fun addSticker(sticker: Sticker,
                   @Sticker.Position position: Int,save:String,stickerNo: String ): StickerView {
        if(stickerNo!=""){
            sticker.dataType=stickerNo
        }
        if (ViewCompat.isLaidOut(this)) {
            addStickerImmediately(sticker, position,save)
        } else {
            post { addStickerImmediately(sticker, position, save) }
        }
        return this
    }

    protected fun addStickerImmediately(
        sticker: Sticker, @Sticker.Position position: Int,
        save: String
    ) {
//        setStickerPosition(sticker, position)
        val scaleFactor: Float
        val widthScaleFactor: Float
        val heightScaleFactor: Float
//        widthScaleFactor = width.toFloat() / 60
//        heightScaleFactor = height.toFloat() / 60
//        scaleFactor = if (widthScaleFactor > heightScaleFactor) heightScaleFactor else widthScaleFactor
        if(save=="") {
//            sticker.matrix
//                .postScale(
//                    scaleFactor / 2,
//                    scaleFactor / 2,
//                    width / 2.toFloat(),
//                    height / 2.toFloat()
//                )
        }
        currentSticker = sticker
        stickers.add(sticker)
        if (onStickerOperationListener != null) {
            onStickerOperationListener!!.onStickerAdded(sticker)
        }
        invalidate()
    }

    protected fun setStickerPosition(sticker: Sticker, @Sticker.Position position: Int) {
//        if(isTextOrImage.equals("name")){
//            width
//        }
        val width = width.toFloat()
        val height = height.toFloat()
        var offsetX = width - sticker.getWidth()
        var offsetY = height - sticker.getHeight()
        if (position and Sticker.Position.TOP > 0) {
            offsetY /= 4f
        } else if (position and Sticker.Position.BOTTOM > 0) {
            offsetY *= 3f / 4f
        } else {
            offsetY /= 2f
        }
        if (position and Sticker.Position.LEFT > 0) {
            offsetX /= 4f
        } else if (position and Sticker.Position.RIGHT > 0) {
            offsetX *= 3f / 4f
        } else {
            offsetX /= 2f
        }
        sticker.matrix.postTranslate(150f,-20f)
    }

    fun getStickerPoints(sticker: Sticker?): FloatArray {
        val points = FloatArray(8)
        getStickerPoints(sticker, points)
        return points
    }

    fun getStickerPoints(sticker: Sticker?, dst: FloatArray) {
        if (sticker == null) {
            Arrays.fill(dst, 0f)
            return
        }
        sticker.getBoundPoints(bounds)
        sticker.getMappedPoints(dst, bounds)
    }

    fun save(file: File) {
        try {
//            saveImageToGallery(file, createBitmap())
//            notifySystemGallery(context, file)
        } catch (ignored: IllegalArgumentException) { //
        } catch (ignored: IllegalStateException) {
        }
    }

    @Throws(OutOfMemoryError::class)
    fun createBitmap(): Bitmap {
        currentSticker = null
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        return bitmap
    }

    val stickerCount: Int
        get() = stickers.size

    val isNoneSticker: Boolean
        get() = stickerCount == 0

    fun setLocked(locked: Boolean): StickerView {
        isLocked = locked
        invalidate()
        return this
    }

    fun setMinClickDelayTime(minClickDelayTime: Int): StickerView {
        this.minClickDelayTime = minClickDelayTime
        return this
    }

    fun setConstrained(constrained: Boolean): StickerView {
        isConstrained = constrained
        postInvalidate()
        return this
    }

    fun setOnStickerOperationListener(
            onStickerOperationListener: OnStickerOperationListener?): StickerView {
        this.onStickerOperationListener = onStickerOperationListener
        return this
    }

    /*fun getIcons(): List<BitmapStickerIcon> {
        return icons
    }*/

    fun setIcons(icons: Collection<BitmapStickerIcon>) {
        this.icons.clear()
        this.icons.addAll(icons)
        invalidate()
    }

    interface OnStickerOperationListener {
        fun onStickerAdded(sticker: Sticker)
        fun onStickerClicked(sticker: Sticker)
        fun onStickerDeleted(sticker: Sticker)
        fun onStickerDragFinished(sticker: Sticker)
        fun onStickerTouchedDown(sticker: Sticker)
        fun onStickerZoomFinished(sticker: Sticker)
        fun onStickerFlipped(sticker: Sticker)
        fun onStickerDoubleTapped(sticker: Sticker)
    }

    companion object {
        private const val TAG = "StickerView"
        private const val DEFAULT_MIN_CLICK_DELAY_TIME = 200
        const val FLIP_HORIZONTALLY = 1
        const val FLIP_VERTICALLY = 1 shl 1
    }

    init {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
        var a: TypedArray? = null
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.StickerView)
            showIcons = a.getBoolean(R.styleable.StickerView_showIcons, false)
            showBorder = a.getBoolean(R.styleable.StickerView_showBorder, false)
            bringToFrontCurrentSticker = a.getBoolean(R.styleable.StickerView_bringToFrontCurrentSticker, false)
            borderPaint.isAntiAlias = true
            borderPaint.color = a.getColor(R.styleable.StickerView_borderColor, Color.BLACK)
            borderPaint.alpha = a.getInteger(R.styleable.StickerView_borderAlpha, 128)
//            configDefaultIcons()
        } finally {
            a?.recycle()
        }
    }
}