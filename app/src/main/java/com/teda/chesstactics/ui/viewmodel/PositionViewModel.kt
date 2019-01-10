package com.teda.chesstactics.ui.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.teda.chesstactics.App
import com.teda.chesstactics.data.DataRepository
import com.teda.chesstactics.data.entity.Elo
import com.teda.chesstactics.data.entity.Position

class PositionViewModel(application: Application) : AndroidViewModel(application) {

    var dataRepository: DataRepository
    val position: MutableLiveData<Position> = MutableLiveData()
    var elo: LiveData<Elo>?
    var listElo: LiveData<List<Elo>>?

    init {
        dataRepository = (application as App).getRepository()
        elo = dataRepository.getElo()
        listElo = dataRepository.getElos()
    }

    fun getPosition(): LiveData<Position> {
        return position
    }

    fun getNewPosition(minElo: Int, maxElo: Int) {
        dataRepository.getPosition(minElo, maxElo, position)
    }

    fun updatePosition(position: Position) {
        dataRepository.updatePosition(position)
    }

    fun resetLastSolution() {
        dataRepository.resetPositions()
    }

    fun insertOrUpdateElo(elo: Elo) {
        dataRepository.insertElo(elo)
    }

}