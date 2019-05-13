package com.teda.chesstactics.ui.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.teda.chesstactics.App
import com.teda.chesstactics.data.DataRepository
import com.teda.chesstactics.data.entity.Position

class DifficultyViewModel(application: Application) : AndroidViewModel(application) {

    private val dataRepository: DataRepository by lazy { (application as App).getRepository() }
    val positions: MutableLiveData<List<Position>> = MutableLiveData()
    private var currentPosition: MutableLiveData<Int> = MutableLiveData()

    fun getPositionsRange(minElo: Int, maxElo: Int) {
        dataRepository.getPositionsRange(minElo, maxElo, positions)
    }

    fun setCurrentPosition(position: Int) {
        currentPosition.postValue(position)
    }

}