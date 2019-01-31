package com.teda.chesstactics.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.teda.chesstactics.Utilities
import com.teda.chesstactics.data.entity.Elo
import com.teda.chesstactics.data.entity.Group
import com.teda.chesstactics.data.entity.GroupPositions
import com.teda.chesstactics.data.entity.Position
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class DataRepository(db: CDatabase) {

    var db: CDatabase? = null

    init {
        this.db = db
    }

    companion object {
        private var INSTANCE: DataRepository? = null
        fun getInstance(db: CDatabase): DataRepository? {
            if (INSTANCE == null) {
                synchronized(DataRepository::class) {
                    INSTANCE = DataRepository(db)
                }
            }
            return INSTANCE
        }
    }

    fun getPosition(minElo: Int, maxElo: Int, livePosition: MutableLiveData<Position>) {
        thread {
            //            livePosition.postValue(db?.positionDao()?.getPosition(minElo, maxElo))
            val format = SimpleDateFormat("dd/MM/yyyy")
            val date = format.parse(format.format(Date()))
            val iDate = date.time

            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.DATE, 1)
            val lDate = calendar.time.time
            livePosition.postValue(db?.positionDao()?.getPositionDate(iDate, lDate))
        }
    }

    fun updatePosition(position: Position) {
        thread(start = true) {
            db?.positionDao()?.update(position)
        }
    }

    fun getPositions(): List<Position>? {
        return db?.positionDao()?.getPositions()
    }

    fun getLikedPositions(): LiveData<List<Position>>? {
        return db?.positionDao()?.getLikedPositions()
    }

    fun resetPositions(livePosition: MutableLiveData<Position>) {
        thread {
            db?.positionDao()?.resetLastSolution()
            getPosition(1500, 2000, livePosition)
        }
    }

    fun getElo(): LiveData<Elo>? {
        return db?.eloDao()?.getCurrentElo()
    }

    fun insertElo(elo: Elo) {
        elo.sDate = Utilities.getStringDate()
        elo.date = Date()
        thread(start = true) {
            db?.eloDao()?.insert(elo)
        }
    }

    fun getElos(): LiveData<List<Elo>>? {
        return db?.eloDao()?.getElos()
    }

    fun getElosByDate(days: Int?, mutableElo: MutableLiveData<List<Elo>>) {
        val format = SimpleDateFormat("dd/MM/yyyy")
        val date = format.parse(format.format(Date()))

        val calendar = Calendar.getInstance()
        calendar.time = date
        thread {
            days?.let {
                calendar.add(Calendar.DATE, -it)
                val lDate = calendar.time.time
                mutableElo.postValue(db?.eloDao()?.getElosDate(Date(lDate)))
            } ?: run {
                mutableElo.postValue(db?.eloDao()?.getListElo())
            }
        }
    }

    fun getGroups(): LiveData<List<Group>>? {
        return db?.groupDao()?.getGroups()
    }

    fun getGroupDetails(id: Int): LiveData<GroupPositions>? {
        return db?.groupDao()?.getGroupDetails(id)
    }

}