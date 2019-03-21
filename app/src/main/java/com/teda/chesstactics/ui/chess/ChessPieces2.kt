package com.teda.chesstactics.ui.chess

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.media.MediaPlayer
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.teda.chesstactics.App
import com.teda.chesstactics.Constants
import com.teda.chesstactics.R
import com.teda.chesstactics.Utilities
import com.teda.chesstactics.data.entity.Position
import com.teda.chesstactics.ui.Piece
import kotlinx.android.synthetic.main.dialog_piece_promotion.view.*


class ChessPieces2 : View {

    private var movements = Movements()

    private var squareWidth: Int? = 0
    private val paint by lazy { Paint() }
    private val squarePaint by lazy { Paint() }
    private var padding: Int = 0
    private var drawHighlight = false
    private var onFinished = false

    private var pieces = ArrayList<Piece>()
    private var lastValidPieces = ArrayList<Piece>()
    private var highlights = ArrayList<Pair<Int, Int>>()
    private var selectedPiece: Piece? = null
    private lateinit var problem: Position
    private var move = 0
    private var chessCallback: ChessCallback? = null
    private var movementPosition: Pair<Int, Int>? = null
    private var FLIP_VALUE = 7
    private var ANIMATION_TIME = 350

    //    animation
    private lateinit var valueAnimator: ValueAnimator
    private var animActive = false
    private var positionDestiny: Pair<Int, Int>? = null
    private var startTime: Long = -1
    private var endTime: Long = -1
    private val mHandler = Handler()
    private lateinit var animationBitmap: Bitmap
    private lateinit var animationCanvas: Canvas
    private var copied = false

    private var flip: Boolean = false
    var sound: Boolean = false
    private var validatePawnPromotion = true
    private var currentAnimationTime: Double = 0.0

    private var dialog: AlertDialog? = null

    //    sounds
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
        movements.highlights = highlights
        paint.color = ContextCompat.getColor(context, R.color.blackAlpha)
        squarePaint.color = ContextCompat.getColor(context, R.color.squareHighlight)
        valueAnimator = ValueAnimator.ofInt()
        valueAnimator.setIntValues(0, 100)
        valueAnimator.duration = ANIMATION_TIME.toLong()
//        valueAnimator.interpolator = AccelerateInterpolator()
        valueAnimator.addUpdateListener { animation ->
            currentAnimationTime = animation.animatedValue as Int / 100.0
            invalidate()
        }
        valueAnimator.addListener(endListener)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        squareWidth = MeasureSpec.getSize(widthMeasureSpec) / 8
        padding = squareWidth!! / 16
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (animActive) {
            animatePieceMovement(canvas)
            return
        }
        if (movementPosition != null)
            drawMovementCircle(canvas)
        if (selectedPiece != null)
            drawHighLights(canvas)
        if (drawHighlight)
            drawMovementHighlight(canvas)
        drawPieces(canvas)
    }

    private fun drawPieces(canvas: Canvas?) {
        pieces.forEach {
            val left = (squareWidth!! * (it.position!!.first)) + padding
            val top = (squareWidth!! * (it.position!!.second)) + padding
            val right = (left + squareWidth!!) - (padding * 2)
            val bottom = (top + squareWidth!!) - (padding * 2)
            val drawable = it.rDrawable!!
            drawable?.setBounds(left, top, right, bottom)
            drawable?.draw(canvas)
        }
    }

    private fun drawHighLights(canvas: Canvas?) {
        highlights.clear()
        movements.getHighLights(selectedPiece, true)
        highlights.forEach {
            val left = squareWidth!! * (it.first).toFloat()
            val top = squareWidth!! * (it.second).toFloat()
//            val right = left + squareWidth!!
//            val bottom = top + squareWidth!!
            canvas?.drawCircle(left + squareWidth!! / 2, top + squareWidth!! / 2, squareWidth!! * 0.2F, paint)
        }
        drawSquareHighlight(canvas)
    }

    private fun drawSquareHighlight(canvas: Canvas?) {
        val left = squareWidth!! * (selectedPiece!!.position!!.first).toFloat()
        val top = squareWidth!! * (selectedPiece!!.position!!.second).toFloat()
        val right = left + squareWidth!!
        val bottom = top + squareWidth!!
        canvas?.drawRect(left, top, right, bottom, squarePaint)
    }

    private fun drawMovementHighlight(canvas: Canvas?) {
        var resultPiece = Piece()
        val hPiece = pgnToPiece(problem.movements[move])
        if (flip && !problem.whiteToPlay)
            hPiece.position = Pair(Math.abs(hPiece.position!!.first - FLIP_VALUE),
                    Math.abs(hPiece.position!!.second - FLIP_VALUE))

        val pieces = pieces.filter {
            it.isWhite == problem.whiteToPlay
        }.filter {
            it.pieceType == hPiece.pieceType
        }
        for (piece in pieces) {
            movements.getHighLights(piece, false)
            val highlights = movements.highlights
            val result = highlights.filter {
                it == hPiece.position
            }
            if (result.isNotEmpty()) {
                resultPiece = piece
                break
            }
        }
        if (resultPiece.position != null) {
            val left = squareWidth!! * (resultPiece.position!!.first).toFloat()
            val top = squareWidth!! * (resultPiece.position!!.second).toFloat()
            val right = left + squareWidth!!
            val bottom = top + squareWidth!!
            canvas?.drawRect(left, top, right, bottom, squarePaint)
        }
    }

    private fun drawMovementCircle(canvas: Canvas?) {
        val left = squareWidth!! * (movementPosition!!.first).toFloat()
        val top = squareWidth!! * (movementPosition!!.second).toFloat()
        canvas?.drawCircle(left + squareWidth!! / 2, top + squareWidth!! / 2, squareWidth!!.toFloat(), paint)
    }

    private fun animatePieceMovement(canvas: Canvas?) {
        if (!copied) {
            animationBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            animationCanvas = Canvas(animationBitmap)
            pieces.forEach {
                if (it.position != selectedPiece!!.position) {
                    val left = (squareWidth!! * (it.position!!.first)) + padding
                    val top = (squareWidth!! * (it.position!!.second)) + padding
                    val right = (left + squareWidth!!) - (padding * 2)
                    val bottom = (top + squareWidth!!) - (padding * 2)
                    val drawable = it.rDrawable!!
                    drawable?.setBounds(left, top, right, bottom)
                    drawable?.draw(animationCanvas)
                }
            }
            copied = true
        }
        canvas?.drawBitmap(animationBitmap, 0f, 0f, null)
//        val now = System.currentTimeMillis()
//        var current = ((100 - (endTime - now) * 100 / (ANIMATION_TIME)) / 100.0)
        /* if (current >= 1) {
             current = 1.0
             animActive = false
         }*/
//        val time = System.nanoTime()
        val endLeft = (squareWidth!! * (positionDestiny!!.first)) + padding
        val endTop = (squareWidth!! * (positionDestiny!!.second)) + padding
        val left = (squareWidth!! * (selectedPiece!!.position!!.first)) + padding
        val top = (squareWidth!! * (selectedPiece!!.position!!.second)) + padding
        val increaseLeft = (left + ((endLeft - left) * currentAnimationTime)).toInt()
        val increaseTop = (top + ((endTop - top) * currentAnimationTime)).toInt()
        val right = (increaseLeft + squareWidth!!) - (padding * 2)
        val bottom = (increaseTop + squareWidth!!) - (padding * 2)
        val drawable = selectedPiece!!.rDrawable!!
        drawable.setBounds(increaseLeft, increaseTop, right, bottom)
        drawable.draw(canvas)
    }

    private fun getPiece(x: Float, y: Float) {
        selectedPiece?.let {
            val piece = getPieceByPosition(x, y)
            if (piece != null && piece != selectedPiece && piece.isWhite == problem.whiteToPlay) {
                selectedPiece = piece
                invalidate()
            } else {
                movePiece(x, y)
            }
        } ?: run {
            val piece = getPieceByPosition(x, y)
            if (piece != null)
                if (piece.isWhite == problem.whiteToPlay)
                    selectedPiece = piece
            invalidate()
        }
    }

    private fun getPieceByPosition(x: Float, y: Float): Piece? {
        val position = getChessPosition(x, y)
        val filteredPieces = pieces.filter {
            it.position!!.first == position.first
        }.filter {
            it.position!!.second == position.second
        }
        return if (filteredPieces.isEmpty()) null else filteredPieces.first()
    }

    private fun movePiece(x: Float, y: Float) {
        if (move >= problem.movements.size)
            return
        val destiny = pgnToPiece(problem.movements[move])
        val position = getChessPosition(x, y)
        if (flip && !problem.whiteToPlay)
            destiny.position = Pair(Math.abs(destiny.position!!.first - FLIP_VALUE), Math.abs(destiny.position!!.second - FLIP_VALUE))
        if (validatePawnPromotion) {
            if (selectedPiece?.pieceType == PieceType.PAWN && (position.second == 7 || position.second == 0)) {
                showPromotionDialog(x, y)
                return
            }
        } else
            validatePawnPromotion = true
        if (destiny.pieceType == selectedPiece?.pieceType && destiny.position == position) {
            movements.movePiece(position)
            move += 1
            selectedPiece = null
            highlights.clear()
            invalidate()
            playSound()
            saveLastPosition()
            moveAnswer()
        } else {
            if (highlights.any { it == position }) {
                playSound()
                movements.movePiece(position)
                chessCallback?.onMoveError()
                onFinished = true
            }
            selectedPiece = null
            highlights.clear()
            invalidate()
        }
    }

    private fun moveAnswer() {
        if (move < problem.movements.size) {
            val pgnPiece = pgnToPiece(problem.movements[move])
            if (flip && !problem.whiteToPlay)
                pgnPiece.position = Pair(Math.abs(pgnPiece.position!!.first - FLIP_VALUE),
                        Math.abs(pgnPiece.position!!.second - FLIP_VALUE))

            val filteredPieces = pieces.filter { it.pieceType == pgnPiece.pieceType }
                    .filter { it.isWhite != problem.whiteToPlay }
            for (p in filteredPieces) {
                movements.getHighLights(p, false)
                if (highlights.contains(pgnPiece.position)) {
                    animActive = true
                    selectedPiece = p
                    startTime = System.currentTimeMillis()
                    endTime = startTime + ANIMATION_TIME
                    positionDestiny = pgnPiece.position
//                    mHandler.postDelayed({
                    startMovementAnimation()
//                    }, 200)

                    /*selectedPiece = p
                    movements.movePiece(pgnPiece.position!!)
                    move += 1
                    selectedPiece = null
                    highlights.clear()
                    invalidate()*/
                    break
                }
            }
            saveLastPosition()
        } else {
            onFinished = true
            chessCallback?.onProblemSolved()
        }
    }

    private fun pgnToPiece(initMove: String): Piece {
        var move = initMove
        val piece = Piece()
        val x: Int
        val y: Int
        if (!move[0].isUpperCase()) {
            var size = move.length - 1
            piece.pieceType = PieceType.PAWN
            if (move.contains("""=""")) {
                val position = move.indexOf("""=""")
                piece.pieceType = PieceFEN.pngPieceMap[move[position + 1].toString()]
                move = move.substring(0, size - 1)
                size = move.length - 1
            }
            x = PieceFEN.pngLetterMap[move[size - 1].toString()]!!
            y = PieceFEN.pngLetterMap[move[size].toString()]!!

        } else {
            piece.pieceType = PieceFEN.pngPieceMap[move[0].toString()]
            x = PieceFEN.pngLetterMap[move[1].toString()]!!
            y = PieceFEN.pngLetterMap[move[2].toString()]!!
        }
        piece.position = Pair(x, y)
        return piece
    }

    private fun getChessPosition(x: Float, y: Float): Pair<Int, Int> {
        val positionX = (x / squareWidth!!).toInt()
        val positionY = (y / squareWidth!!).toInt()
        return Pair(positionX, positionY)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        if (onFinished || animActive)
            return false
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> if (selectedPiece?.position != getChessPosition(event.x, event.y)) {
                drawHighlight = false
                getPiece(event.x, event.y)
            }
            MotionEvent.ACTION_UP -> {
                movementPosition = null
                if (selectedPiece?.position != getChessPosition(event.x, event.y) && selectedPiece != null)
                    getPiece(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> if (selectedPiece != null && highlights.isNotEmpty()) {
                val position = getChessPosition(event.x, event.y)
                val check = highlights.filter {
                    it == position
                }
                if (check.isEmpty() && movementPosition != null) {
                    movementPosition = null
                    invalidate()
                } else if (check.isNotEmpty() && position != movementPosition) {
                    movementPosition = position
                    invalidate()
                }
            }
        }
        return true
    }

    private fun setChessPieces(pieces: ArrayList<Piece>) {
        this.pieces = ArrayList(pieces)
        movements.pieces = this.pieces
        invalidate()
    }

    fun setChessProblem(problem: Position) {
        this.problem = problem
        this.problem.setMovements()
        val pieces = Utilities.getPieces(context, problem.initialPosition)
        if (!problem.whiteToPlay) {
            flip = App.prefs!!.getBoolean(Constants.KEY_FLIP_BOARD, true)
            if (flip && !problem.whiteToPlay) {
                pieces.forEach {
                    it.position = Pair(Math.abs(it.position!!.first - FLIP_VALUE), Math.abs(it.position!!.second - FLIP_VALUE))
                }
            }
        } else {
            flip = false
        }
        if (animActive)
            valueAnimator.cancel()
        /* for (piece in pieces) {
             piece.rDrawable = ContextCompat.getDrawable(context, piece.drawable!!)
         }*/
        setChessPieces(pieces)
        saveLastPosition()
        highlights.clear()
        drawHighlight = false
        selectedPiece = null
        onFinished = false
        move = 0
        movements.isWhitePuzzle = problem.whiteToPlay
    }

    fun retryProblem() {
        onFinished = false
        pieces.clear()
        lastValidPieces.forEach {
            val piece = Piece()
            piece.copy(it)
            pieces.add(piece)
        }
        setChessPieces(pieces)
        highlights.clear()
        selectedPiece = null
    }

    fun setChessCallbackListener(chessCallback: ChessCallback) {
        this.chessCallback = chessCallback
    }

    private fun saveLastPosition() {
        /*lastValidPieces = ArrayList(pieces)
        var pos = lastValidPieces.filter {
            it.position == selectedPiece!!.position
        }
        val piece = Piece()
        piece.copy(pos.first())*/
        lastValidPieces.clear()
        pieces.forEach {
            val piece = Piece()
            piece.copy(it)
            lastValidPieces.add(piece)
        }
    }

    fun showHighlight() {
        if (move < problem.movements.size && !onFinished && !animActive) {
            selectedPiece = null
            highlights.clear()
            drawHighlight = true
            invalidate()
        }
    }

    private fun playSound() {
        if (sound) {
            val mp = MediaPlayer.create(context, R.raw.move)
            mp.setOnCompletionListener {
                it.reset()
                it.release()
            }
            mp.start()
            /*val assetFileDescriptor = context.resources.openRawResourceFd(R.raw.move) ?: return
            mediaPlayer.run {
                reset()
                setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.declaredLength)
                prepareAsync()
        }*/
        }
    }

    fun setFlip(flip: Boolean) {
        this.flip = flip
        movements.flip = this.flip
    }

    private fun showPromotionDialog(x: Float, y: Float) {
        if (dialog != null && dialog!!.isShowing)
            return
        val builder = AlertDialog.Builder(context)
        val view = inflate(context, R.layout.dialog_piece_promotion, null)
        view.linearQueen.setOnClickListener {
            updatePawn(x, y, PieceType.QUEEN, dialog)
        }
        view.linearRook.setOnClickListener {
            updatePawn(x, y, PieceType.ROOK, dialog)
        }
        view.linearBishop.setOnClickListener {
            updatePawn(x, y, PieceType.BISHOP, dialog)
        }
        view.linearKnight.setOnClickListener {
            updatePawn(x, y, PieceType.KNIGHT, dialog)
        }
        builder.setTitle(R.string.select_piece)
        dialog = builder.setView(view).create()
        dialog?.show()
    }

    private fun updatePawn(x: Float, y: Float, type: PieceType, dialog: AlertDialog?) {
        validatePawnPromotion = false
        selectedPiece?.pieceType = type
        selectedPiece?.drawable = Utilities.getPieceDrawable(selectedPiece!!.isWhite, selectedPiece!!.pieceType!!)
        selectedPiece?.rDrawable = ContextCompat.getDrawable(context, selectedPiece?.drawable!!)
        movePiece(x, y)
        dialog?.dismiss()
    }

    private fun startMovementAnimation() {
        valueAnimator.start()
    }

    private val endListener = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)
            animActive = false
            copied = false
            movements.movePiece(positionDestiny!!)
            move += 1
            selectedPiece = null
            positionDestiny = null
            highlights.clear()
            invalidate()
        }

        override fun onAnimationCancel(animation: Animator?) {
            animActive = false
            copied = false
        }
    }

    interface ChessCallback {

        fun onMoveError()

        fun onProblemSolved()

    }
}