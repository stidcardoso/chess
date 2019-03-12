package com.teda.chesstactics.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.chip.Chip
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.teda.chesstactics.R
import com.teda.chesstactics.data.entity.Elo
import com.teda.chesstactics.ui.viewmodel.ProgressViewModel
import kotlinx.android.synthetic.main.fragment_progress.*
import java.text.SimpleDateFormat
import java.util.*


class ProgressFragment() : Fragment() {

    var model: ProgressViewModel? = null
    lateinit var preferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_progress, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProviders.of(this).get(ProgressViewModel::class.java)
        model?.getElos()?.observe(this, Observer {
            for (elo in it!!) {
                Log.d("elo", elo.elo.toString())
                Log.d("date", elo.sDate)
            }
            styleLineChart()
            setupData(it)
        })
        model?.currentElo?.observe(this, Observer { elo ->
            elo?.let { textElo.text = it.elo.toInt().toString() }
        })
        chipGroup.setOnCheckedChangeListener { chipGroup, _ ->
            var chip = chipGroup.getChildAt(chipGroup.checkedChipId)
            chip?.let {
                chip = it as Chip
            }
            for (i in 0 until chipGroup.childCount) {
                val chip = chipGroup.getChildAt(i)
                chip.isClickable = chip.id != chipGroup.checkedChipId
            }
            chip?.isClickable = false
        }
        chipWeek.isClickable = false

        chipWeek.setOnClickListener {
            model?.getElosByDate(7)
        }
        chipMonth.setOnClickListener {
            model?.getElosByDate(30)
        }
        chipAll.setOnClickListener {
            model?.getElosByDate(null)
        }
    }

    override fun onResume() {
        super.onResume()
        preferences = PreferenceManager.getDefaultSharedPreferences(activity)
        if (preferences.getBoolean("keyScreenOn", false))
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun styleLineChart() {
        lineChart.description = null
        lineChart.xAxis.setDrawLabels(false)
        lineChart.axisRight.setDrawLabels(false)
//        lineChart.xAxis.gridColor = ContextCompat.getColor(activity!!, R.color.gridColor)
        lineChart.axisLeft.gridColor = ContextCompat.getColor(activity!!, R.color.gridColor)
//        lineChart.axisRight.gridColor = ContextCompat.getColor(activity!!, R.color.gridColor)
        lineChart.setDrawBorders(false)
        lineChart.xAxis.setDrawGridLines(false)
        lineChart.xAxis.setDrawAxisLine(false)
        lineChart.axisLeft.setDrawAxisLine(false)
        lineChart.axisRight.setDrawAxisLine(false)
        lineChart.legend.isEnabled = false
    }

    private fun setupData(elos: List<Elo>) {
        val format = SimpleDateFormat("dd/MM/yyyy")
        val date = format.parse(format.format(Date()))

        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, -30)
        val iDate = calendar.time.time

        val entries = arrayListOf<Entry>()
        for (elo in elos) {
            val x = ((elo.date?.time!! - iDate) / (60 * 60 * 24 * 1000))
//            val date = format.parse(format.format(Date(x)))
//            x = date.time
            entries.add(Entry(x.toFloat(), elo.elo.toFloat()))
        }
        val line = LineDataSet(entries, "")
        line.setDrawValues(false)
        line.lineWidth = 3F
        line.color = ContextCompat.getColor(activity!!, R.color.lineColor)
        line.setDrawCircleHole(false)
        line.circleRadius = 5F
        line.setCircleColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
        val lines = arrayListOf<ILineDataSet>()
        lines.add(line)
        val data = LineData(lines)
        lineChart.data = data
        lineChart.invalidate()

    }

}