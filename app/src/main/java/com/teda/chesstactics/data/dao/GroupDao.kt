package com.teda.chesstactics.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.teda.chesstactics.data.entity.Group
import com.teda.chesstactics.data.entity.GroupPositions

@Dao
interface GroupDao {

    @Insert
    fun insertGroup(group: Group): Long

    @Query("select * from `group` where available = 1")
    fun getGroups(): List<Group>

    @Query("select * from `group` where available = 0")
    fun getNewGroups(): LiveData<List<Group>>

    @Query("select * from `group` where id = :id")
    fun getGroupDetails(id: Int): LiveData<GroupPositions>

//    *100 / (select Count(*) from position where groupId = :id)

    @Query("select (Count(*)*100 / (select Count(*) from position where groupId = :id)) from position where groupId = :id and groupSolved = 1")
    fun getSolutionPercentage(id: Int): Int

  /*  @Query("select Count(*) from position where groupId = :id")
    fun getSolutionPercentage2(id: Int): Int*/


}