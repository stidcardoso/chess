package com.teda.chesstactics.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "position")
class Position : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var initialPosition: String = ""
    var pgn = ""
    var elo: Int = 0
    var difficulty: String = ""
    var whiteToPlay: Boolean = true
    var lastSolution: Date? = null
    var liked: Boolean = false
    var groupId: Int? = null
    var groupSolved = false
    @Ignore
    var movements = arrayListOf<String>()
//    var solutions: ArrayList<Solution> = arrayListOf()

    fun setMovements() {
        val nMovements = pgn.split(" ")
        val re = Regex("[x+#?!]")
        for (mov in nMovements) {
            if (!mov.contains("""."""))
                movements.add(mov.replace(re, ""))
        }
    }

    fun copy(position: Position) {
        this.id = position.id
        this.initialPosition = position.initialPosition
        this.pgn = position.pgn
        this.elo = position.elo
        this.difficulty = position.difficulty
        this.whiteToPlay = position.whiteToPlay
        this.lastSolution = position.lastSolution
        this.liked = position.liked
        this.groupId = position.groupId
        this.groupSolved = position.groupSolved
    }
}