package com.teda.chesstactics.ui.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.teda.chesstactics.App
import com.teda.chesstactics.data.DataRepository
import com.teda.chesstactics.data.entity.Position

class CountDownViewModel(application: Application) : AndroidViewModel(application) {

    private val dataRepository: DataRepository by lazy { (application as App).getRepository() }
    val position: MutableLiveData<Position> = MutableLiveData()

    fun getPosition(): LiveData<Position> {
        return position
    }

    fun getNewPosition(minElo: Int, maxElo: Int) {
        dataRepository.getPosition(minElo, maxElo, position)
    }
}