package com.teda.chesstactics.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.teda.chesstactics.Constants
import com.teda.chesstactics.GridSpace
import com.teda.chesstactics.R
import com.teda.chesstactics.ui.adapter.MAdapter
import com.teda.chesstactics.ui.viewmodel.CategoriesViewModel
import kotlinx.android.synthetic.main.fragment_categories.*

class CategoriesFragment() : Fragment() {

    companion object {
        const val TAG = "CATEGORIES_FRAGMENT"
        fun newInstance(): CategoriesFragment {
            return CategoriesFragment()
        }
    }

    private lateinit var categoriesViewModel: CategoriesViewModel
    private lateinit var mAdapter: MAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*cardEasy.setOnClickListener { }
        cardMedium.setOnClickListener { }
        cardHard.setOnClickListener { }*/
        val request = AdRequest.Builder().build()
        adView.loadAd(request)
        recyclerPackages.layoutManager = GridLayoutManager(activity, 3)
        recyclerPackages.addItemDecoration(GridSpace(8))
        mAdapter = MAdapter(arrayListOf())
        recyclerPackages.adapter = mAdapter
        categoriesViewModel = ViewModelProviders.of(this).get(CategoriesViewModel::class.java)
        categoriesViewModel.groups.observe(this, Observer {
            mAdapter.list = ArrayList(it)
            mAdapter.notifyDataSetChanged()
        })
        btnOneMinute.setOnClickListener {
            startCountDownActivity(1)
        }
        btnThreeMinute.setOnClickListener {
            startCountDownActivity(3)
        }
        btnFiveMinute.setOnClickListener {
            startCountDownActivity(5)
        }
        btnEasy.setOnClickListener { goToDifficultyActivity(Constants.EASY) }
        btnMedium.setOnClickListener { goToDifficultyActivity(Constants.MEDIUM) }
        btnHard.setOnClickListener { goToDifficultyActivity(Constants.HARD) }
        fabBuy.setOnClickListener {
            startActivity(Intent(activity, BuyGroupsActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        categoriesViewModel.getGroups()
    }

    private fun startCountDownActivity(minutes: Int) {
        val i = Intent(activity!!, CountDownActivity::class.java)
        i.putExtra(Constants.EXTRAS_TIME_COUNT_DOWN, minutes)
        startActivity(i)
    }

    private fun goToDifficultyActivity(difficulty: String) {
        val intent = Intent(activity, DifficultyActivity::class.java)
        intent.putExtra(Constants.EXTRAS_DIFFICULTY, difficulty)
        startActivity(intent)
    }
}