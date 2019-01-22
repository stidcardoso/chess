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

    @Query("select * from `group`")
    fun getGroups(): LiveData<List<Group>>

    @Query("select * from `group` where id = :id")
    fun getGroupDetails(id: Int): LiveData<GroupPositions>

}