package com.teda.chesstactics.data.entity

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

class GroupPositions {

    @Embedded
    var group: Group? = null

    @Relation(parentColumn = "id", entityColumn = "groupId", entity = Position::class)
    var positions: List<Position>? = null

}