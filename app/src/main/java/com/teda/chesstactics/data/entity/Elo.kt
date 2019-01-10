package com.teda.chesstactics.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "elo",
        indices = [Index(value = ["sDate"], unique = true)])

class Elo {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    var sDate: String = ""
    var date: Date? = null
    var elo: Double = 0.0
}