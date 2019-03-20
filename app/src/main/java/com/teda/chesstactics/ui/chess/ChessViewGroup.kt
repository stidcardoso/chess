package com.teda.chesstactics.ui.chess

import android.content.Context
import android.graphics.Point
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.teda.chesstactics.R


class ChessViewGroup : ViewGroup {

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private var screenWidth: Int = 0
    private var squareWidth: Int = 0

    fun init() {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenWidth = size.x
        squareWidth = screenWidth / 8
//        val height = size.y

        for (x in 0 until 4) {
            for (y in 0 until 4) {
                val v = View(context)
                v.background = ContextCompat.getDrawable(context, R.drawable.ic_wking)
                v.x = (squareWidth * x).toFloat()
                v.y = (squareWidth * y).toFloat()
                val params = LayoutParams(screenWidth / 8, screenWidth / 8)

                addView(v, params)
            }
        }
        /*     movements.highlights = highlights
             paint.color = ContextCompat.getColor(context, R.color.blackAlpha)
             squarePaint.color = ContextCompat.getColor(context, R.color.squareHighlight)*/
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
//        for (i in 0..childCount) {
//            val child = getChildAt(i)
        /* child.layoutParams.width = widthMeasureSpec / 8
         child.layoutParams.height = widthMeasureSpec / 8*/
//        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }


    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        Log.d("count", childCount.toString())
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child?.layout(0, 0, child.measuredWidth, child.measuredHeight)
        }
    }


}