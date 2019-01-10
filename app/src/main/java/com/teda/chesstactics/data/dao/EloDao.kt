package com.teda.chesstactics.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.teda.chesstactics.data.entity.Elo

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

}