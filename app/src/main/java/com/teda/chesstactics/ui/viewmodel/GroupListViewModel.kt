package com.teda.chesstactics.ui.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.teda.chesstactics.App
import com.teda.chesstactics.data.DataRepository
import com.teda.chesstactics.data.entity.GroupPositions

class GroupListViewModel(application: Application) : AndroidViewModel(application) {

    private val dataRepository: DataRepository by lazy { (application as App).getRepository() }
    var group: LiveData<GroupPositions>? = null

    fun getGroup(id: Int): LiveData<GroupPositions>? {
        return dataRepository.getGroupDetails(id)
    }
}

