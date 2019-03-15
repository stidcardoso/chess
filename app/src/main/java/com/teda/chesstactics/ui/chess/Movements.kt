package com.teda.chesstactics.ui.chess

import com.teda.chesstactics.ui.Piece

class Movements {

    var pieces = ArrayList<Piece>()
    var highlights = ArrayList<Pair<Int, Int>>()
    private var selectedPiece: Piece? = null
    lateinit var pos: Pair<Int, Int>
    var king: Piece? = Piece()
    private var attackedSquares = ArrayList<Pair<Int, Int>>()
    var flip = true
    var isWhitePuzzle = true

    fun getHighLights(selectedPiece: Piece?, check: Boolean) {
        this.pos = selectedPiece?.position
                ?: run { Pair(0, 0) }

        this.selectedPiece = selectedPiece
        if (selectedPiece?.pieceType!! == PieceType.PAWN) {
            getPawnHighlights(false)
        }
        if (selectedPiece.pieceType!! == PieceType.KNIGHT)
            getKnightHighlights()
        if (selectedPiece.pieceType!! == PieceType.BISHOP)
            getBishopHighlights()
        if (selectedPiece.pieceType!! == PieceType.ROOK)
            getRookHighlights()
        if (selectedPiece.pieceType!! == PieceType.QUEEN) {
            getBishopHighlights()
            getRookHighlights()
        }
        if (selectedPiece.pieceType!! == PieceType.KING)
            getKingHighlights(check)
    }

    private fun getAttackedSquares(selectedPiece: Piece?) {
        this.pos = selectedPiece?.position
                ?: run { Pair(0, 0) }

        this.selectedPiece = selectedPiece
        if (selectedPiece?.pieceType!! == PieceType.PAWN) {
            getPawnHighlights(true)
        }
        if (selectedPiece.pieceType!! == PieceType.KNIGHT)
            getKnightHighlights()
        if (selectedPiece.pieceType!! == PieceType.BISHOP)
            getBishopHighlights()
        if (selectedPiece.pieceType!! == PieceType.ROOK)
            getRookHighlights()
        if (selectedPiece.pieceType!! == PieceType.QUEEN) {
            getBishopHighlights()
            getRookHighlights()
        }
        if (selectedPiece.pieceType!! == PieceType.KING)
            getKingHighlights(false)
    }

    private fun getPawnHighlights(attacked: Boolean) {
        val pos = selectedPiece?.position
                ?: run { Pair(0, 0) }
        val upSide = isUpSide()
        if (attacked) {
            if (selectedPiece!!.isWhite && !flip) {
                highlights.add(Pair(pos.first + 1, pos.second - 1))
                highlights.add(Pair(pos.first - 1, pos.second - 1))
            } else {
                highlights.add(Pair(pos.first + 1, pos.second + 1))
                highlights.add(Pair(pos.first - 1, pos.second + 1))
            }
            return
        }

        if (!upSide) {
            /* if (attacked) {
                 highlights.add(Pair(pos.first + 1, pos.second - 1))
                 highlights.add(Pair(pos.first - 1, pos.second - 1))
                 return
             }*/
            if (!getSinglePiece(pos.first, pos.second - 1)) {
                highlights.add(Pair(pos.first, pos.second - 1))
                if (pos.second == 6 && !getSinglePiece(pos.first, pos.second - 2)) {
                    highlights.add(Pair(pos.first, pos.second - 2))
                }
            }
            attackPiece(pos.first + 1, pos.second - 1)
            attackPiece(pos.first - 1, pos.second - 1)
        } else {
            /*if (attacked) {
                highlights.add(Pair(pos.first + 1, pos.second + 1))
                highlights.add(Pair(pos.first - 1, pos.second + 1))
                return
            }*/
            if (!getSinglePiece(pos.first, pos.second + 1)) {
                highlights.add(Pair(pos.first, pos.second + 1))
                if (pos.second == 1 && !getSinglePiece(pos.first, pos.second + 2)) {
                    highlights.add(Pair(pos.first, pos.second + 2))
                }
            }
            attackPiece(pos.first + 1, pos.second + 1)
            attackPiece(pos.first - 1, pos.second + 1)
        }
    }

    private fun getKnightHighlights() {
        for (i in -2..2) {
            val y = if ((i % 2) == 0) 1 else 2
            if (i != 0)
                if (!attackPiece(pos.first - i, pos.second + y))
                    highlights.add(Pair(pos.first - i, pos.second + y))
        }
        for (i in -2..2) {
            val y = if ((i % 2) == 0) 1 else 2
            if (i != 0)
                if (!attackPiece(pos.first - i, pos.second - y))
                    highlights.add(Pair(pos.first - i, pos.second - y))
        }
    }

    private fun getBishopHighlights() {
        var y = pos.second
        for (i in pos.first + 1..7) {
            y += 1
            if (attackPiece(i, y))
                break
            else
                highlights.add(Pair(i, y))
        }
        y = pos.second
        for (i in pos.first - 1 downTo 0) {
            y -= 1
            if (attackPiece(i, y))
                break
            else
                highlights.add(Pair(i, y))
        }
        var x = pos.first
        for (i in pos.second + 1..7) {
            x -= 1
            if (attackPiece(x, i))
                break
            else
                highlights.add(Pair(x, i))
        }
        x = pos.first
        for (i in pos.second - 1 downTo 0) {
            x += 1
            if (attackPiece(x, i))
                break
            else
                highlights.add(Pair(x, i))
        }
    }

    private fun getRookHighlights() {
        val pos = selectedPiece?.position
                ?: run { Pair(0, 0) }

        for (i in pos.first + 1..7) {
            if (attackPiece(i, pos.second))
                break
            else
                highlights.add(Pair(i, pos.second))
        }

        for (i in pos.first - 1 downTo 0) {
            if (attackPiece(i, pos.second))
                break
            else
                highlights.add(Pair(i, pos.second))
        }

        for (i in pos.second + 1..7) {
            if (attackPiece(pos.first, i))
                break
            else
                highlights.add(Pair(pos.first, i))
        }

        for (i in pos.second - 1 downTo 0) {
            if (attackPiece(pos.first, i))
                break
            else
                highlights.add(Pair(pos.first, i))
        }
    }

    private fun getKingHighlights(check: Boolean) {
        attackedSquares.clear()
        if (check) {
            king?.copy(selectedPiece!!)
            getEnemyAttackedSquares()
            selectedPiece = king
            this.pos = selectedPiece?.position
                    ?: run { Pair(0, 0) }
        }
        for (i in -1..1) {
            for (j in -1..1) {
                if (!attackedSquares.contains(Pair(pos.first - i, pos.second - j)))
                    if (!attackPiece(pos.first - i, pos.second - j))
                        highlights.add(Pair(pos.first - i, pos.second - j))
            }
        }

//        for (i in -1..1) {
//            if (!attackPiece(pos.first - i, pos.second - 1))
//                highlights.add(Pair(pos.first - i, pos.second - 1))
//        }
    }

    private fun getSinglePiece(x: Int, y: Int): Boolean {
        val piece = pieces.filter {
            it.position?.first == x
        }.filter {
            it.position?.second == y
        }
        return !piece.isEmpty()
    }

    private fun attackPiece(x: Int, y: Int): Boolean {
        val piece = pieces.filter {
            it.position?.first == x
        }.filter {
            it.position?.second == y
        }
        if (piece.isEmpty())
            return false
        else
            if (selectedPiece!!.isWhite != piece.first().isWhite) {
                highlights.add(Pair(x, y))
                return true
            }
        return true
    }

    fun getEnemyAttackedSquares() {
        pieces.forEach {
            if (king!!.isWhite != it.isWhite) {
                getAttackedSquares(it)
            }
        }
        attackedSquares = ArrayList(highlights)
        highlights.clear()
    }

    fun movePiece(position: Pair<Int, Int>) {
        val highlight = highlights.filter {
            it.first == position.first
        }.filter {
            it.second == position.second
        }

        if (highlight.isNotEmpty()) {
            val removePieces = pieces.filter { it.position!! == position }
            if (removePieces.isNotEmpty())
                pieces.remove(removePieces.first())
            for (piece in pieces) {
                if (piece.position?.first == selectedPiece?.position?.first && piece.position?.second == selectedPiece?.position?.second) {
                    piece.pieceType = selectedPiece?.pieceType
                    piece.drawable = selectedPiece?.drawable
                    piece.position = position
                }
            }
        }

    }

    private fun isUpSide(): Boolean {
        return if (isWhitePuzzle) {
            !selectedPiece!!.isWhite
        } else {
            (selectedPiece!!.isWhite == flip)
        }
        /*  return if(selectedPiece!!.isWhite && ((move % 2) == 0))
              true
          else
              false*/
    }

}