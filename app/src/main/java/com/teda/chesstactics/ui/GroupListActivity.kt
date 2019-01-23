package com.teda.chesstactics.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.teda.chesstactics.Constants
import com.teda.chesstactics.GridSpace
import com.teda.chesstactics.R
import com.teda.chesstactics.ui.adapter.NumberAdapter
import com.teda.chesstactics.ui.viewmodel.GroupListViewModel
import kotlinx.android.synthetic.main.activity_group_list.*

class GroupListActivity : AppCompatActivity() {

    private lateinit var groupListViewModel: GroupListViewModel
    private val adapter by lazy { NumberAdapter(arrayListOf()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_list)
        val groupId = intent.extras.getInt(Constants.EXTRAS_GROUP_ID, 0)
        groupListViewModel = ViewModelProviders.of(this).get(GroupListViewModel::class.java)
        groupListViewModel.getGroup(groupId)?.observe(this, Observer {
            adapter.positions = it?.positions!!
            adapter.notifyDataSetChanged()
        })
        groupListViewModel.getGroup(groupId)
        recyclerNumbers.layoutManager = GridLayoutManager(this, 4)
        recyclerNumbers.addItemDecoration(GridSpace(8))
        recyclerNumbers.adapter = adapter
//        groupListViewModel.getGroup(groupId)
    }

}