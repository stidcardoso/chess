package com.teda.chesstactics.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "position")
class Position {

    @PrimaryKey
    var id: Int = 0
    var pgn = ""
    var initialPosition: String = ""
    var elo: Int = 0
    var difficulty: String = ""
    var whiteToPlay: Boolean = true
    var movements = arrayListOf<String>()
    var lastSolution: Date? = null
    var liked: Boolean = false
//    var solutions: ArrayList<Solution> = arrayListOf()

    fun setMovements() {
        val nMovements = pgn.split(" ")
        for (mov in nMovements) {
            if (!mov.toString().contains("""."""))
                movements.add(mov.replace("x", ""))
        }
    }
}