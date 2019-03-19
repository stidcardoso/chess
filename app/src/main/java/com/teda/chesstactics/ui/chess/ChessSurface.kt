package com.teda.chesstactics.ui.chess

import android.content.Context
import android.graphics.Canvas
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.teda.chesstactics.Utilities
import com.teda.chesstactics.data.entity.Position
import com.teda.chesstactics.ui.Piece


class ChessSurface : SurfaceView, Runnable {

    override fun run() {
        while (mRunning) {
            if (mHolder.surface.isValid) {
                var canvas = mHolder.lockCanvas()
                drawPieces(canvas)
                mHolder.unlockCanvasAndPost(canvas)
            }
        }
    }

    private lateinit var mHolder: SurfaceHolder
    private lateinit var thread: Thread
    private var mRunning = true

    private var squareWidth: Int? = 0
    private var padding: Int = 0

    private lateinit var problem: Position
    private var pieces = ArrayList<Piece>()
    private var movements = Movements()

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun init() {
        mHolder = holder
//        mHolder.addCallback(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        squareWidth = MeasureSpec.getSize(widthMeasureSpec) / 8
        padding = squareWidth!! / 16
    }

    /* override fun surfaceCreated(p0: SurfaceHolder?) {
         val canvas = holder.lockCanvas()
         canvas?.let {
             drawPieces(it)
             holder.unlockCanvasAndPost(it)
         }
     }

     override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {

     }

     override fun surfaceDestroyed(p0: SurfaceHolder?) {
     }*/

    fun setChessProblem(problem: Position) {
        this.problem = problem
        this.problem.setMovements()
        val pieces = Utilities.getPieces(problem.initialPosition)
        if (!problem.whiteToPlay) {
//            flip = App.prefs!!.getBoolean(Constants.KEY_FLIP_BOARD, true)
//            if (flip && !problem.whiteToPlay) {
//                pieces.forEach {
//                    it.position = Pair(Math.abs(it.position!!.first - FLIP_VALUE), Math.abs(it.position!!.second - FLIP_VALUE))
//                }
//            }
        } else {
//            flip = false
        }
        setChessPieces(pieces)
//        saveLastPosition()
//        highlights.clear()
//        drawHighlight = false
//        selectedPiece = null
//        onFinished = false
//        move = 0
//        movements.isWhitePuzzle = problem.whiteToPlay
    }

    private fun setChessPieces(pieces: ArrayList<Piece>) {
        this.pieces = ArrayList(pieces)
        movements.pieces = this.pieces
        invalidate()
    }


    private fun drawPieces(canvas: Canvas) {
        pieces.forEach {
            val left = (squareWidth!! * (it.position!!.first)) + padding
            val top = (squareWidth!! * (it.position!!.second)) + padding
            val right = (left + squareWidth!!) - (padding * 2)
            val bottom = (top + squareWidth!!) - (padding * 2)
            val drawable = ContextCompat.getDrawable(context, it.drawable!!)
            drawable?.setBounds(left, top, right, bottom)
            drawable?.draw(canvas)
            /*    var anim = ObjectAnimator.ofPropertyValuesHolder(drawable, PropertyValuesHolder.ofInt("alpha", 255))
                anim.target = drawable
                anim.duration = 10000
                anim.start()
             canvas?.drawRect(left, top, right, bottom, paint)*/
        }
//        for(i in 0..pieces.size) {

//
    }

    fun pause() {
        mRunning = false
        try {
            // Stop the thread == rejoin the main thread.
            thread.join()
        } catch (e: InterruptedException) {
        }
    }

    /**
     * Called by MainActivity.onResume() to start a thread.
     */
    fun resume() {
        mRunning = true
        thread = Thread(this);
        thread.start();
    }


}
