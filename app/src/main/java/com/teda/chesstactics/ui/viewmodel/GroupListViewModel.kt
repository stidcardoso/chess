package com.teda.chesstactics.ui.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.teda.chesstactics.App
import com.teda.chesstactics.data.DataRepository
import com.teda.chesstactics.data.entity.GroupPositions

class GroupListViewModel(application: Application) : AndroidViewModel(application) {

    private val dataRepository: DataRepository by lazy { (application as App).getRepository() }
    var group: LiveData<GroupPositions>? = null
     var currentPosition: MutableLiveData<Int> = MutableLiveData()

    fun getGroup(id: Int): LiveData<GroupPositions>? {
        group = dataRepository.getGroupDetails(id)
        return group
    }

    fun setCurrentPosition(position: Int) {
        currentPosition.postValue(position)
    }
}

