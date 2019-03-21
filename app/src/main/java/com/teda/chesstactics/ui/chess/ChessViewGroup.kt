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
import com.teda.chesstactics.Utilities
import com.teda.chesstactics.data.entity.Position
import com.teda.chesstactics.ui.Piece


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

    var problem: Position? = null
    var pieces: ArrayList<Piece> = arrayListOf()


    fun init() {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenWidth = size.x
        squareWidth = screenWidth / 8
//        val height = size.y

        /* for (x in 0 until 4) {
             for (y in 0 until 4) {
                 val v = View(context)
                 v.background = ContextCompat.getDrawable(context, R.drawable.ic_wking)
                 v.x = (squareWidth * x).toFloat()
                 v.y = (squareWidth * y).toFloat()
                 val params = LayoutParams(screenWidth / 8, screenWidth / 8)

                 addView(v, params)
             }
         }*/
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

    fun setChessProblem(problem: Position) {
        this.problem = problem
        this.problem!!.setMovements()
        val pieces = Utilities.getPieces(context, problem.initialPosition)
        this.pieces = pieces
        addViews()
        /*if (!problem.whiteToPlay) {
            flip = App.prefs!!.getBoolean(Constants.KEY_FLIP_BOARD, true)
            if (flip && !problem.whiteToPlay) {
                pieces.forEach {
                    it.position = Pair(Math.abs(it.position!!.first - FLIP_VALUE), Math.abs(it.position!!.second - FLIP_VALUE))
                }
            }
        } else {
            flip = false
        }*/
//        setChessPieces(pieces)
//        saveLastPosition()
//        highlights.clear()
//        drawHighlight = false
//        selectedPiece = null
//        onFinished = false
//        move = 0
//        movements.isWhitePuzzle = problem.whiteToPlay
    }

    private fun addViews() {
        for (piece in pieces) {
            val v = View(context)
            v.background = ContextCompat.getDrawable(context, piece.drawable!!)
            v.x = (squareWidth * piece.position!!.first.toFloat())
            v.y = (squareWidth * piece.position!!.second.toFloat())
            val params = LayoutParams(screenWidth / 8, screenWidth / 8)
            addView(v, params)
        }
        requestLayout()
    }


}