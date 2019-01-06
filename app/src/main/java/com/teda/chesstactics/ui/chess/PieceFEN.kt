package com.teda.chesstactics.ui.chess

import com.teda.chesstactics.R

object PieceFEN {
    const val WKING = "K"
    const val WQUEEN = "Q"
    const val WROOK = "R"
    const val WBISHOP = "B"
    const val WKNIGHT = "N"
    const val WPAWN = "P"
    const val BKING = "k"
    const val BQUEEN = "q"
    const val BROOK = "r"
    const val BBISHOP = "b"
    const val BKNIGHT = "n"
    const val BPAWN = "p"

    const val IMAGE_WKING = R.drawable.ic_wking
    const val IMAGE_WQUEEN = R.drawable.ic_wqueen
    const val IMAGE_WROOK = R.drawable.ic_wrook
    const val IMAGE_WBISHOP = R.drawable.ic_wbishop
    const val IMAGE_WKNIGHT = R.drawable.ic_wknight
    const val IMAGE_WPAWN = R.drawable.ic_wpawn
    const val IMAGE_BKING = R.drawable.ic_bking
    const val IMAGE_BQUEEN = R.drawable.ic_bqueen
    const val IMAGE_BROOK = R.drawable.ic_brook
    const val IMAGE_BBISHOP = R.drawable.ic_bbishop
    const val IMAGE_BKNIGHT = R.drawable.ic_bknight
    const val IMAGE_BPAWN = R.drawable.ic_bpawn

    val pngPieceMap: HashMap<String, PieceType> = hashMapOf(
            "K" to PieceType.KING,
            "Q" to PieceType.QUEEN,
            "R" to PieceType.ROOK,
            "B" to PieceType.BISHOP,
            "N" to PieceType.KNIGHT
    )

    val pngLetterMap: HashMap<String, Int> = hashMapOf(
            "a" to 0,
            "b" to 1,
            "c" to 2,
            "d" to 3,
            "e" to 4,
            "f" to 5,
            "g" to 6,
            "h" to 7,
            "8" to 0,
            "7" to 1,
            "6" to 2,
            "5" to 3,
            "4" to 4,
            "3" to 5,
            "2" to 6,
            "1" to 7
    )


}