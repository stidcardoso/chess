package com.teda.chesstactics.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import com.teda.chesstactics.Constants
import com.teda.chesstactics.R
import com.teda.chesstactics.data.entity.GroupPositions
import com.teda.chesstactics.ui.adapter.NumberAdapter
import com.teda.chesstactics.ui.viewmodel.GroupListViewModel
import kotlinx.android.synthetic.main.activity_group_list.*

class GroupListActivity : AppCompatActivity() {

    private lateinit var groupListViewModel: GroupListViewModel
    private val adapter by lazy { NumberAdapter(arrayListOf()) }
    private lateinit var mToolbar: Toolbar
    private var positionFragment: PositionFragment? = null
    private var groupPositions: GroupPositions? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_list)
        setupToolbar()
        val groupId = intent.extras.getInt(Constants.EXTRAS_GROUP_ID, 0)
        groupListViewModel = ViewModelProviders.of(this).get(GroupListViewModel::class.java)
        groupListViewModel.getGroup(groupId)?.observe(this, Observer {
            groupPositions = it
            adapter.positions = it?.positions!!
            adapter.notifyDataSetChanged()
            mToolbar.title = it.group?.name
        })
        groupListViewModel.currentPosition.observe(this, Observer {
            setSubtitle(it)
        })
        groupListViewModel.getGroup(groupId)
        recyclerNumbers.layoutManager = GridLayoutManager(this, 6)
//        recyclerNumbers.addItemDecoration(GridSpace(4))
        recyclerNumbers.adapter = adapter
        adapter.clickSubject.subscribe {
            val ft = supportFragmentManager.beginTransaction()
            positionFragment = PositionFragment.newInstance(it)
            groupListViewModel.setCurrentPosition(it)
            ft.replace(R.id.frameContainer, positionFragment)
            ft.addToBackStack(null)
            ft.commit()
//            positionFragment.startProblem(groupPositions?.positions!![it])
        }
//        groupListViewModel.getGroup(groupId)
    }

    private fun setupToolbar() {
        mToolbar = toolbar as Toolbar
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setSubtitle(position: Int) {
        val subtitle = (position + 1).toString() + "/" + groupPositions?.positions?.size.toString()
        mToolbar.subtitle = subtitle
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}