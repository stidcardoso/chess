package com.teda.chesstactics.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import com.teda.chesstactics.Constants
import com.teda.chesstactics.GridSpace
import com.teda.chesstactics.R
import com.teda.chesstactics.ui.adapter.DifficultyAdapter
import com.teda.chesstactics.ui.viewmodel.DifficultyViewModel
import kotlinx.android.synthetic.main.activity_group_list.*

class DifficultyActivity : AppCompatActivity() {

    private var minElo = 0
    private var maxElo = 1900
    private lateinit var difficulty: String
    private lateinit var difficultyViewModel: DifficultyViewModel
    private var positionFragment: PositionFragment? = null
    private val adapter by lazy { DifficultyAdapter(arrayListOf()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_list)
        difficultyViewModel = ViewModelProviders.of(this).get(DifficultyViewModel::class.java)
        recyclerNumbers.layoutManager = GridLayoutManager(this, 6)
        recyclerNumbers.addItemDecoration(GridSpace(8))
        recyclerNumbers.adapter = adapter
        difficulty = intent.extras.getString(Constants.EXTRAS_DIFFICULTY, Constants.EASY)
        when (difficulty) {
            Constants.EASY -> {
                setupToolbar(getString(R.string.easy))
                minElo = 0
                maxElo = 1900
            }
            Constants.MEDIUM -> {
                setupToolbar(getString(R.string.medium))
                minElo = 1901
                maxElo = 2100
            }
            Constants.HARD -> {
                setupToolbar(getString(R.string.hard))
                minElo = 2101
                maxElo = 3000
            }
        }
        difficultyViewModel.positions.observe(this, Observer {
            adapter.positions = it ?: arrayListOf()
            adapter.notifyDataSetChanged()
        })
        difficultyViewModel.getPositionsRange(minElo, maxElo)

        adapter.clickSubject.subscribe {
            val ft = supportFragmentManager.beginTransaction()
            positionFragment = PositionFragment.newInstance(it)
            ft.replace(R.id.frameContainer, positionFragment!!)
            ft.addToBackStack(null)
            ft.commit()
        }
    }

    private fun setupToolbar(title: String) {
        val toolbar = toolbar as Toolbar
        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}