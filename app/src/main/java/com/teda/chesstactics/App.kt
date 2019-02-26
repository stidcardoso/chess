package com.teda.chesstactics

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.teda.chesstactics.data.CDatabase
import com.teda.chesstactics.data.DataRepository

class App : Application() {

    companion object {
        var prefs: SharedPreferences? = null
    }

    override fun onCreate() {
        super.onCreate()
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
    }

    fun getDatabase(): CDatabase {
        return CDatabase.getInstance(this)!!
    }

    fun getRepository(): DataRepository {
        return DataRepository.getInstance(getDatabase())!!
    }

}