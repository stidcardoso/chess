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

    private var dataRepository: DataRepository = (application as App).getRepository()
    val position: MutableLiveData<Position> = MutableLiveData()
    var elo: LiveData<Elo>?
    private var listElo: LiveData<List<Elo>>?
    private var eloRange = 500

    init {
        elo = dataRepository.getElo()
        listElo = dataRepository.getElos()
    }

    fun getPosition(): LiveData<Position> {
        return position
    }

    fun getNewPosition(currentElo: Elo?) {
        currentElo?.let {
            dataRepository.getPosition(it.elo.toInt() - eloRange, it.elo.toInt() + eloRange, position)
        }
    }

    fun updatePosition(position: Position) {
        dataRepository.updatePosition(position)
    }

    fun resetLastSolution() {
        dataRepository.resetPositions(position)
    }

    fun insertOrUpdateElo(elo: Elo) {
        dataRepository.insertElo(elo)
    }

}