package com.teda.chesstactics.data.entity

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

class PackagePositions {

    @Embedded
    var group: Package? = null

    @Relation(parentColumn = "id", entityColumn = "packageId", entity = Position::class)
    var positions: List<Position>? = null

}