package com.teda.chesstactics.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.teda.chesstactics.Constants
import com.teda.chesstactics.R
import kotlinx.android.synthetic.main.fragment_categories.*

class CategoriesFragment : Fragment() {

    companion object {
        fun newInstance(): CategoriesFragment {
            return CategoriesFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*cardEasy.setOnClickListener { }
        cardMedium.setOnClickListener { }
        cardHard.setOnClickListener { }*/
        btnOneMinute.setOnClickListener {
            startCountDownActivity(1)
        }
        btnThreeMinute.setOnClickListener {
            startCountDownActivity(3)
        }
        btnFiveMinute.setOnClickListener {
            startCountDownActivity(5)
        }
    }

    private fun startCountDownActivity(minutes: Int) {
        val i = Intent(activity!!, CountDownActivity::class.java)
        i.putExtra(Constants.EXTRAS_TIME_COUNT_DOWN, minutes)
        startActivity(i)
    }
}