package com.teda.chesstactics.data

class Problem {

    var initialPosition: String = ""
    var difficulty: String = ""
    var whiteToPlay: Boolean = true
    var pgn = ""
    var movements = arrayListOf<String>()
//    var solutions: ArrayList<Solution> = arrayListOf()

    fun setMovements() {
        val nMovements = pgn.split(" ")
        for (mov in nMovements) {
            if (!mov.toString().contains("""."""))
                movements.add(mov.replace("x", ""))
        }
    }
}