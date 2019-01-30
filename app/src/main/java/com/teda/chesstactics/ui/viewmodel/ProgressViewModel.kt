package com.teda.chesstactics.ui.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.teda.chesstactics.App
import com.teda.chesstactics.data.DataRepository
import com.teda.chesstactics.data.entity.Elo

class ProgressViewModel(application: Application) : AndroidViewModel(application) {

    private val dataRepository: DataRepository by lazy { (application as App).getRepository() }
    var currentElo: LiveData<Elo>? = null
    var elos: LiveData<List<Elo>>? = null

    init {
        currentElo = dataRepository.getElo()
        elos= dataRepository.getElosByDate(30)
    }




}

