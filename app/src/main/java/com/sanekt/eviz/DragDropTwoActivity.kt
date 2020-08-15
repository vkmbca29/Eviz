package com.sanekt.eviz

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.item_view.*


class DragDropTwoActivity : AppCompatActivity(), View.OnLongClickListener, View.OnDragListener {

    private var screenHeight = 0
    private var screenWidth = 0
    private lateinit var iv: ImageView
    private lateinit var tv: TextView
    private lateinit var rl: RelativeLayout

    private val TAG = DragDropTwoActivity::class.java.simpleName

    private var xVal = 0
    private var yVal = 0

    private var txVal = 0
    private var tyVal = 0


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag_drop_two)


        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenHeight = displayMetrics.heightPixels
        screenWidth = displayMetrics.widthPixels

        iv = findViewById(R.id.iv)
        tv = findViewById(R.id.tv)
        tv.text=intent.getStringExtra("text")

        rl = findViewById(R.id.rl)
        iv.setOnLongClickListener(this)
        tv.setOnLongClickListener(this)


        rl.setOnDragListener(this)

        rl.setOnClickListener {

            if(xVal == 0 && yVal == 0){
                xVal = iv.x.toInt()
                yVal = iv.y.toInt()

            }

            if(txVal == 0 && tyVal == 0){
                txVal = tv.x.toInt()
                tyVal = tv.y.toInt()

            }
            startActivity(
                Intent(this, ViewDragDropActivity::class.java)
                .putExtra("x", xVal)
                .putExtra("y", yVal)
                .putExtra("tx", txVal)
                .putExtra("ty", tyVal)
                .putExtra("text", tv.text.toString())
            )
        }
    }

    override fun onLongClick(v: View?): Boolean {
        val dragShadowBuilder = View.DragShadowBuilder(v)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            v?.startDragAndDrop(null, dragShadowBuilder, v, 0)
        } else {
            v?.startDrag(null, dragShadowBuilder, v, 0)
        }
//            v?.startDrag(null, dragShadowBuilder, v, 0)

        return true
    }


//    override fun onTouch(view:View, motionEvent: MotionEvent):Boolean {
//        Log.d(TAG, "onTouch: view->view$view\n MotionEvent$motionEvent")
//        return if (motionEvent.action === MotionEvent.ACTION_DOWN) {
//            val dragShadowBuilder = View.DragShadowBuilder(view)
//            view.startDrag(null, dragShadowBuilder, view, 0)
//            true
//        } else {
//            false
//        }
//    }

    override fun onDrag(v: View?, event: DragEvent?): Boolean {

        var newX: Float
        var newY: Float

        when (event!!.action) {
            DragEvent.ACTION_DRAG_ENDED -> {
                Log.d(TAG, "onDrag: ACTION_DRAG_ENDED ")
                return true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                Log.d(TAG, "onDrag: ACTION_DRAG_EXITED")
                return true
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                Log.d(TAG, "onDrag: ACTION_DRAG_ENTERED")
                return true
            }
            DragEvent.ACTION_DRAG_STARTED -> {
                Log.d(TAG, "onDrag: ACTION_DRAG_STARTED")
                return true
            }
            DragEvent.ACTION_DROP -> {
                Log.d(TAG, "onDrag: ACTION_DROP")
                val tvState = event.localState as View
                Log.d(TAG, "onDrag:viewX" + event.x + "viewY" + event.y)
                Log.d(TAG, "onDrag: Owner->" + tvState.parent)
                val tvParent = tvState.parent as ViewGroup
                tvParent.removeView(tvState)
                val container = v as RelativeLayout
                container.addView(tvState)
                tvParent.removeView(tvState)
                tvState.x = event.x
                tvState.y = event.y
                v.addView(tvState)
                v.setVisibility(View.VISIBLE)

                if(tvState is TextView) {
                    txVal = event.x.toInt()
                    tyVal = event.y.toInt()
                }else if(tvState is ImageView) {
                    xVal = event.x.toInt()
                    yVal = event.y.toInt()
                }


                return true
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                Log.d(TAG, "onDrag: ACTION_DRAG_LOCATION")
                return true
            }
            else -> return false
        }
    }
}