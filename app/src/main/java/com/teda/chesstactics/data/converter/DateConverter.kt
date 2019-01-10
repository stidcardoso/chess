package com.teda.chesstactics.data.converter

import android.arch.persistence.room.TypeConverter
import java.util.*

class DateConverter {

    @TypeConverter
    fun toDate(timeStamp: Long?): Date? {
        return timeStamp?.let {
            Date(it)
        }
    }

    @TypeConverter
    fun toTimeStamp(date: Date?): Long? {
        return date?.time
    }
}