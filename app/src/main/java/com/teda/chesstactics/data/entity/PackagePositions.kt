package com.teda.chesstactics.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "packagePosition")
class PackagePositions {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0


}