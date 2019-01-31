package com.teda.chesstactics.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.teda.chesstactics.data.entity.Elo
import java.util.*

@Dao
interface EloDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(elo: Elo)

    @Update
    fun update(elo: Elo)

    @Query("SELECT * from elo order by id desc limit 1")
    fun getCurrentElo(): LiveData<Elo>

    @Query("SELECT * FROM elo")
    fun getElos(): LiveData<List<Elo>>

    @Query("SELECT * FROM elo")
    fun getListElo(): List<Elo>

    @Query("SELECT * from elo where date >= :minDate")
    fun getElosDate(minDate: Date?): List<Elo>

}