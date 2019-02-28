package com.teda.chesstactics.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "group")
class Group {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var name: String = ""
    var image: Int = 0
    var available = false
    @Ignore
    var percentage: Int = 0
    @Ignore
    var positions: List<Position>? = null
}