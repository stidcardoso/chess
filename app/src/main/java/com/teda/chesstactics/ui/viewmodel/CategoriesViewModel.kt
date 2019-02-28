package com.teda.chesstactics.ui.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.teda.chesstactics.App
import com.teda.chesstactics.data.DataRepository
import com.teda.chesstactics.data.entity.Group

class CategoriesViewModel(application: Application) : AndroidViewModel(application) {

    private val dataRepository: DataRepository by lazy { (application as App).getRepository() }
    var groups: MutableLiveData<List<Group>> = MutableLiveData()

    init {
        getGroups()
    }

    fun getGroups() {
        dataRepository.getGroups(groups)
    }


}