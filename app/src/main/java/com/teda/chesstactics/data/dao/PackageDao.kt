package com.teda.chesstactics.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.teda.chesstactics.data.entity.Package
import com.teda.chesstactics.data.entity.PackagePositions

interface PackageDao {

    @Insert
    fun insertPackage(group: Package)

    @Query("select * from package")
    fun getPackages(): LiveData<List<Package>>

    @Query("select * from package where id = :id")
    fun getPackageDetail(id: Int): LiveData<PackagePositions>

}