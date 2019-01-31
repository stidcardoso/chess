package com.teda.chesstactics.ui.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.teda.chesstactics.App
import com.teda.chesstactics.data.DataRepository
import com.teda.chesstactics.data.entity.Elo

class ProgressViewModel(application: Application) : AndroidViewModel(application) {

    private val dataRepository: DataRepository by lazy { (application as App).getRepository() }
    var currentElo: LiveData<Elo>? = null
    private var elos: MutableLiveData<List<Elo>> = MutableLiveData()

    init {
        currentElo = dataRepository.getElo()
        dataRepository.getElosByDate(30, elos)
    }

    fun getElos(): LiveData<List<Elo>> {
        return elos
    }

    fun getElosByDate(days: Int?) {
        dataRepository.getElosByDate(days, elos)
    }


}

