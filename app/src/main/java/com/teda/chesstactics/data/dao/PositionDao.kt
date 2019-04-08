package com.teda.chesstactics.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.teda.chesstactics.data.entity.Position

@Dao
interface PositionDao {

    @Insert
    fun insert(position: Position)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(positions: List<Position>)

    @Update
    fun update(position: Position)

    @Query("Select * from position")
    fun getPositions(): List<Position>

    @Query("Select * from position where liked = 1")
    fun getLikedPositions(): LiveData<List<Position>>

    @Query("Select * from position where (elo between :minElo and :maxElo) order by random() limit 1")
    fun getPosition(minElo: Int, maxElo: Int): Position

    @Query("Select * from position where (elo between :minElo and :maxElo) order by elo ASC")
    fun getPositionsRange(minElo: Int, maxElo: Int): List<Position>

    @Query("Select * from position where (lastSolution is null) or (lastSolution < :iDate or lastSolution > :lDate) order by random() limit 1")
    fun getPositionDate(iDate: Long, lDate: Long): Position

    @Query("Update position set lastSolution = null")
    fun resetLastSolution()

}