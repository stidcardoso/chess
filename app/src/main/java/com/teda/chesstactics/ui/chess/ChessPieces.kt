package com.teda.chesstactics.ui.chess

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.teda.chesstactics.R
import com.teda.chesstactics.Utilities
import com.teda.chesstactics.data.entity.Position
import com.teda.chesstactics.ui.Piece

class ChessPieces : View {

    private var pieces = ArrayList<Piece>()
    private var lastValidPieces = ArrayList<Piece>()
    private var highlights = ArrayList<Pair<Int, Int>>()
    private var squareWidth: Int? = 0
    private val paint by lazy { Paint() }
    private val squarePaint by lazy { Paint() }
    private var selectedPiece: Piece? = null
    private var padding: Int = 0
    private lateinit var problem: Position
    private var move = 0
    private var chessCallback: ChessCallback? = null
    private var movementPosition: Pair<Int, Int>? = null

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
        Movements.highlights = highlights
        paint.color = ContextCompat.getColor(context, R.color.blackAlpha)
        squarePaint.color = ContextCompat.getColor(context, R.color.squareHighlight)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        squareWidth = MeasureSpec.getSize(widthMeasureSpec) / 8
        padding = squareWidth!! / 16
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        squareWidth = width / 8
        if (movementPosition != null) {
            drawMovementCircle(canvas)
        }
        if (selectedPiece != null)
            drawHighLights(canvas)
        drawPieces(canvas)
    }

    private fun drawPieces(canvas: Canvas?) {
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

//        }
    }

    private fun drawHighLights(canvas: Canvas?) {
        highlights.clear()
        Movements.getHighLights(selectedPiece, true)
        highlights.forEach {
            val left = squareWidth!! * (it.first).toFloat()
            val top = squareWidth!! * (it.second).toFloat()
            val right = left + squareWidth!!
            val bottom = top + squareWidth!!
            canvas?.drawCircle(left + squareWidth!! / 2, top + squareWidth!! / 2, squareWidth!! * 0.2F, paint)
        }
        drawSquarehighlight(canvas)
    }

    fun drawSquarehighlight(canvas: Canvas?) {
        val left = squareWidth!! * (selectedPiece!!.position!!.first).toFloat()
        val top = squareWidth!! * (selectedPiece!!.position!!.second).toFloat()
        val right = left + squareWidth!!
        val bottom = top + squareWidth!!
        canvas?.drawRect(left, top, right, bottom, squarePaint)
    }

    private fun drawMovementCircle(canvas: Canvas?) {
        val left = squareWidth!! * (movementPosition!!.first).toFloat()
        val top = squareWidth!! * (movementPosition!!.second).toFloat()
        canvas?.drawCircle(left + squareWidth!! / 2, top + squareWidth!! / 2, squareWidth!!.toFloat(), paint)
    }


    private fun getPiece(x: Float, y: Float) {
        selectedPiece?.let {
            val piece = getPieceByPosition(x, y)
            if (piece != null && piece != selectedPiece && piece.isWhite == problem.whiteToPlay) {
                selectedPiece = piece
                invalidate()
            } else
                movePiece(x, y)
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
        if (move < problem.movements.size) {
            val destiny = pgnToPiece(problem.movements[move])
            val position = getChessPosition(x, y)
            if (destiny.pieceType == selectedPiece?.pieceType && destiny.position == position) {
                Movements.movePiece(position)
                move += 1
                selectedPiece = null
                highlights.clear()
                invalidate()
                saveLastPosition()
                moveAnswer()
            } else {
                if (highlights.filter { it == position }.isNotEmpty()) {
                    Movements.movePiece(position)
                    chessCallback?.onMoveError()
                }
                selectedPiece = null
                highlights.clear()
                invalidate()
            }
        }
    }

    private fun moveAnswer() {
        if (move < problem.movements.size) {
            val pngPiece = pgnToPiece(problem.movements[move])
            val filteredPieces = pieces.filter { it.pieceType == pngPiece.pieceType }
                    .filter { it.isWhite != problem.whiteToPlay }
            var pieceToMove: Piece
            for (p in filteredPieces) {
                Movements.getHighLights(p, false)
                if (highlights.contains(pngPiece.position)) {
                    selectedPiece = p
                    Movements.movePiece(pngPiece.position!!)
                    move += 1
                    selectedPiece = null
                    highlights.clear()
                    invalidate()
                    break
                }
            }
            saveLastPosition()
        } else {
            chessCallback?.onProblemSolved()
        }
    }

    private fun pgnToPiece(move: String): Piece {
        val piece = Piece()
        var x: Int
        var y: Int
        if (!move[0].isUpperCase()) {
            piece.pieceType = PieceType.PAWN
            x = PieceFEN.pngLetterMap[move[0].toString()]!!
            y = PieceFEN.pngLetterMap[move[1].toString()]!!
        } else
            piece.pieceType = PieceFEN.pngPieceMap[move[0].toString()]
        x = PieceFEN.pngLetterMap[move[1].toString()]!!
        y = PieceFEN.pngLetterMap[move[2].toString()]!!
        piece.position = Pair(x, y)
        return piece
    }


    private fun getChessPosition(x: Float, y: Float): Pair<Int, Int> {
        val positionX = (x / squareWidth!!).toInt()
        val positionY = (y / squareWidth!!).toInt()
        val position = Pair(positionX, positionY)
        return position
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        if (event?.action == MotionEvent.ACTION_DOWN) {
            if (selectedPiece?.position != getChessPosition(event.x, event.y))
                getPiece(event.x, event.y)
        } else if (event?.action == MotionEvent.ACTION_UP) {
            movementPosition = null
            if (selectedPiece?.position != getChessPosition(event.x, event.y) && selectedPiece != null)
                getPiece(event.x, event.y)
        } else if (event?.action == MotionEvent.ACTION_MOVE) {
            if (selectedPiece != null && highlights.isNotEmpty()) {
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

    fun setChessPieces(pieces: ArrayList<Piece>) {
        this.pieces = ArrayList(pieces)
        Movements.pieces = this.pieces
        invalidate()
    }

    fun setChessProblem(problem: Position) {
        this.problem = problem
        problem.setMovements()
        val pieces = Utilities.getPieces(problem.initialPosition)
        if (!problem.whiteToPlay) {
            pieces.forEach {
                //                it.isWhite = !it.isWhite
                it.position = Pair(Math.abs(it.position!!.first - 7), Math.abs(it.position!!.second - 7))
            }
        }
        setChessPieces(pieces)
        saveLastPosition()
        highlights.clear()
        selectedPiece = null
        move = 0
    }

    fun retryProblem() {
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

    fun saveLastPosition() {
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

    interface ChessCallback {

        fun onMoveError()

        fun onProblemSolved()

    }

    //    fun setFen(fen: String) {
//        this.fenGame = fen
//        val fenList = fen.split("""/""")
//        for (i in 0..7) {
//            var mPosition = 0
//            for (j in 0..((fenList[i].length - 1))) {
//                val character = fenList[i][j]
//                if (character.isLetter()) {
//                    val piece = getPieceType(character.toString())
//                    val position = Pair(mPosition, i)
//                    piece.position = position
//                    mPosition += 1
//                    pieces.add(piece)
//                } else {
//                    mPosition += (character.toInt() - 1)
//                }
//            }
//        }
//        invalidate()
//    }

//    private fun getPieceType(type: String): Piece {
//        val piece = Piece()
//        when (type) {
//            PieceFEN.WKING -> {
//                piece.pieceType = PieceType.WKING
//                piece.drawable = PieceFEN.IMAGE_WKING
//            }
//            PieceFEN.WQUEEN -> {
//                piece.pieceType = PieceType.WQUEEN
//                piece.drawable = PieceFEN.IMAGE_WQUEEN
//            }
//            PieceFEN.WROOK -> {
//                piece.pieceType = PieceType.WROOK
//                piece.drawable = PieceFEN.IMAGE_WROOK
//            }
//            PieceFEN.WBISHOP -> {
//                piece.pieceType = PieceType.WBISHOP
//                piece.drawable = PieceFEN.IMAGE_WBISHOP
//            }
//            PieceFEN.WKNIGHT -> {
//                piece.pieceType = PieceType.WKNIGHT
//                piece.drawable = PieceFEN.IMAGE_WKNIGHT
//            }
//            PieceFEN.WPAWN -> {
//                piece.pieceType = PieceType.WPAWN
//                piece.drawable = PieceFEN.IMAGE_WPAWN
//            }
//            PieceFEN.BKING -> {
//                piece.pieceType = PieceType.BKING
//                piece.drawable = PieceFEN.IMAGE_BKING
//            }
//            PieceFEN.BQUEEN -> {
//                piece.pieceType = PieceType.BQUEEN
//                piece.drawable = PieceFEN.IMAGE_BQUEEN
//            }
//            PieceFEN.BROOK -> {
//                piece.pieceType = PieceType.BROOK
//                piece.drawable = PieceFEN.IMAGE_BROOK
//            }
//            PieceFEN.BBISHOP -> {
//                piece.pieceType = PieceType.BBISHOP
//                piece.drawable = PieceFEN.IMAGE_BBISHOP
//            }
//            PieceFEN.BKNIGHT -> {
//                piece.pieceType = PieceType.BKNIGHT
//                piece.drawable = PieceFEN.IMAGE_BKNIGHT
//            }
//            PieceFEN.BPAWN -> {
//                piece.pieceType = PieceType.BPAWN
//                piece.drawable = PieceFEN.IMAGE_BPAWN
//            }
//            else -> {
//                PieceType.WPAWN
//            }
//        }
//        return piece
//    }

}
