package com.teda.chesstactics

import android.app.Application
import com.teda.chesstactics.data.CDatabase
import com.teda.chesstactics.data.DataRepository

class App : Application() {


    override fun onCreate() {
        super.onCreate()
    }

    fun getDatabase(): CDatabase {
        return CDatabase.getInstance(this)!!
    }

    fun getRepository(): DataRepository {
        return DataRepository.getInstance(getDatabase())!!
    }

}