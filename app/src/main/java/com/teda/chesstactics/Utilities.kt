package com.teda.chesstactics

import android.content.Context
import com.teda.chesstactics.ui.Piece
import com.teda.chesstactics.ui.chess.PieceFEN
import com.teda.chesstactics.ui.chess.PieceType
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object Utilities {

    fun getPieces(fen: String): ArrayList<Piece> {
        val pieces = arrayListOf<Piece>()
        val fenList = fen.split("""/""")
        for (i in 0..7) {
            var mPosition = 0
            for (j in 0..((fenList[i].length - 1))) {
                val character = fenList[i][j]
                if (character.isLetter()) {
                    val piece = getPieceType(character.toString())
                    val position = Pair(mPosition, i)
                    piece.position = position
                    mPosition += 1
                    pieces.add(piece)
                } else {
                    mPosition += (character.toString().toInt())
                }
            }
        }
        return pieces
    }

    private fun getPieceType(type: String): Piece {
        val piece = Piece()
        when (type) {
            PieceFEN.WKING -> {
                piece.pieceType = PieceType.KING
                piece.isWhite = true
                piece.drawable = PieceFEN.IMAGE_WKING
            }
            PieceFEN.WQUEEN -> {
                piece.pieceType = PieceType.QUEEN
                piece.isWhite = true
                piece.drawable = PieceFEN.IMAGE_WQUEEN
            }
            PieceFEN.WROOK -> {
                piece.pieceType = PieceType.ROOK
                piece.isWhite = true
                piece.drawable = PieceFEN.IMAGE_WROOK
            }
            PieceFEN.WBISHOP -> {
                piece.pieceType = PieceType.BISHOP
                piece.isWhite = true
                piece.drawable = PieceFEN.IMAGE_WBISHOP
            }
            PieceFEN.WKNIGHT -> {
                piece.pieceType = PieceType.KNIGHT
                piece.isWhite = true
                piece.drawable = PieceFEN.IMAGE_WKNIGHT
            }
            PieceFEN.WPAWN -> {
                piece.pieceType = PieceType.PAWN
                piece.isWhite = true
                piece.drawable = PieceFEN.IMAGE_WPAWN
            }
            PieceFEN.BKING -> {
                piece.pieceType = PieceType.KING
                piece.drawable = PieceFEN.IMAGE_BKING
            }
            PieceFEN.BQUEEN -> {
                piece.pieceType = PieceType.QUEEN
                piece.drawable = PieceFEN.IMAGE_BQUEEN
            }
            PieceFEN.BROOK -> {
                piece.pieceType = PieceType.ROOK
                piece.drawable = PieceFEN.IMAGE_BROOK
            }
            PieceFEN.BBISHOP -> {
                piece.pieceType = PieceType.BISHOP
                piece.drawable = PieceFEN.IMAGE_BBISHOP
            }
            PieceFEN.BKNIGHT -> {
                piece.pieceType = PieceType.KNIGHT
                piece.drawable = PieceFEN.IMAGE_BKNIGHT
            }
            PieceFEN.BPAWN -> {
                piece.pieceType = PieceType.PAWN
                piece.drawable = PieceFEN.IMAGE_BPAWN
            }
            else -> {
                PieceType.PAWN
            }
        }
        return piece
    }

    fun getStringDate(): String {
        val format = SimpleDateFormat("dd/MM/yyyy")
        return format.format(Date())
    }

    fun loadJSONFromAsset(context: Context, jsonName: String): String? {
        val json: String?
        try {
            val exist = context.assets.open(jsonName)
            val size = exist.available()
            val buffer = ByteArray(size)
            exist.read(buffer)
            exist.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }


}