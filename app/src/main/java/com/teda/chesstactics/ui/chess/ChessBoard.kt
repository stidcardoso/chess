package com.teda.chesstactics.ui.chess

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.teda.chesstactics.R

class ChessBoard : View {

    val paint = Paint()

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    fun init(set: AttributeSet?) {
        paint.color = ContextCompat.getColor(context, R.color.alphaWhite)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredWidth)
    }

    override fun onDraw(canvas: Canvas?) {
        val squareWidth = width / 8.toFloat()
        for (y in 0..7) {
            for (x in 0..7) {
                if ((x + y) % 2 == 0) {
                    val left = squareWidth * x
                    val right = squareWidth * (x + 1)
                    val top = squareWidth * y
                    val bottom = squareWidth * (y + 1)
                    canvas?.drawRect(left, top, right, bottom, paint)
                }

            }
        }
    }

}