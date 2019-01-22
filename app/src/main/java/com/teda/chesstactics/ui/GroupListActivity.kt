package com.teda.chesstactics.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.teda.chesstactics.Constants
import com.teda.chesstactics.GridSpace
import com.teda.chesstactics.R
import com.teda.chesstactics.ui.viewmodel.GroupListViewModel
import kotlinx.android.synthetic.main.activity_group_list.*

class GroupListActivity : AppCompatActivity() {

    private lateinit var groupListViewModel: GroupListViewModel
    private lateinit var adapter:
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_list)
        var groupId = intent.extras.getInt(Constants.EXTRAS_GROUP_ID, 0)
        groupListViewModel = ViewModelProviders.of(this).get(GroupListViewModel::class.java)
        groupListViewModel.group?.observe(this, Observer {

        })
        groupListViewModel.getGroup(groupId)
        recyclerNumbers.layoutManager = GridLayoutManager(this, 4)
        recyclerNumbers.addItemDecoration(GridSpace(8))
    }

}