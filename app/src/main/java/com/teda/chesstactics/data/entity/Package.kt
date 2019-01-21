package com.teda.chesstactics.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "package")
class Package {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var available = false
}