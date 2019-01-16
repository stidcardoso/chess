package com.teda.chesstactics.ui.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.teda.chesstactics.App
import com.teda.chesstactics.data.DataRepository
import com.teda.chesstactics.data.entity.Position

class CountDownViewModel(application: Application) : AndroidViewModel(application) {

    var dataRepository: DataRepository
    val position: MutableLiveData<Position> = MutableLiveData()

    init {
        dataRepository = (application as App).getRepository()
    }

    fun getPosition(): LiveData<Position> {
        return position
    }

    fun getNewPosition(minElo: Int, maxElo: Int) {
        dataRepository.getPosition(minElo, maxElo, position)
    }
}