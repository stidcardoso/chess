package com.teda.chesstactics.ui

import android.graphics.drawable.Drawable
import com.teda.chesstactics.ui.chess.PieceType

class Piece() {
    var pieceType: PieceType? = null
    var position: Pair<Int, Int>? = null
    var drawable: Int? = null
    var rDrawable: Drawable? = null
    var isWhite: Boolean = false
//    fun isWhite(): Boolean {
//        return pieceType?.ordinal!! <= 5
//    }

    fun copy(piece: Piece) {
        this.pieceType = piece.pieceType
        this.position = piece.position
        this.drawable = piece.drawable
        this.rDrawable = piece.rDrawable
        this.isWhite = piece.isWhite
    }
}