package com.teda.chesstactics.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import com.teda.chesstactics.GridSpace
import com.teda.chesstactics.R
import com.teda.chesstactics.ui.adapter.BuyGroupAdapter
import com.teda.chesstactics.ui.viewmodel.BuyGroupsViewModel
import kotlinx.android.synthetic.main.activity_buy_packages.*

class BuyGroupsActivity : AppCompatActivity() {

    lateinit var adapter: BuyGroupAdapter
    private lateinit var model: BuyGroupsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_packages)
        setupToolbar()
        recyclerPackages.layoutManager = GridLayoutManager(this, 3)
        recyclerPackages.addItemDecoration(GridSpace(8))
        adapter = BuyGroupAdapter(arrayListOf())
        recyclerPackages.adapter = adapter
        model = ViewModelProviders.of(this).get(BuyGroupsViewModel::class.java)
        model.newGroups?.observe(this, Observer {
            adapter.list = ArrayList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun setupToolbar() {
        val toolbar = toolbar as Toolbar
        toolbar.title = getString(R.string.store)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}