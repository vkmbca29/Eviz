package com.sanekt.eviz

import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout


class ViewDragDropActivity : AppCompatActivity() {

    private lateinit var relativeLayout: RelativeLayout
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView

    private lateinit var layoutParams: RelativeLayout.LayoutParams
    private lateinit var layoutParamsOne: RelativeLayout.LayoutParams
    private var xVal: Int = 0
    private var yVal: Int = 0
    private var txVal: Int = 0
    private var tyVal: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_drag_drop)

        relativeLayout = findViewById(R.id.rl)

        imageView = ImageView(this)
        textView = TextView(this)
        imageView.setImageResource(R.mipmap.ic_launcher)
        textView.text = intent!!.getStringExtra("text")

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        xVal = intent!!.getIntExtra("x", 0)
        yVal = intent!!.getIntExtra("y", 0)
        txVal = intent!!.getIntExtra("tx", 0)
        tyVal = intent!!.getIntExtra("ty", 0)

        relativeLayout.addView(imageView)
        relativeLayout.addView(textView)

        layoutParams = imageView.layoutParams as RelativeLayout.LayoutParams
        layoutParamsOne = textView.layoutParams as RelativeLayout.LayoutParams

        layoutParams.leftMargin = xVal
        layoutParams.topMargin = yVal

        layoutParamsOne.leftMargin = txVal
        layoutParamsOne.topMargin = tyVal


        imageView.layoutParams = layoutParams
        textView.layoutParams = layoutParamsOne


    }
}