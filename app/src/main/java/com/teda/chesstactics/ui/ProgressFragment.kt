package com.teda.chesstactics.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class ProgressFragment : Fragment() {

    var model: ProgressViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_progress, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProviders.of(this).get(ProgressViewModel::class.java)
        model?.elos?.observe(this, Observer {
            for (elo in it!!) {
                Log.d("elo", elo.elo.toString())
                Log.d("date", elo.sDate)
            }
            setupData(it)
        })
    }

    fun setupData(elos: List<Elo>) {
        val format = SimpleDateFormat("dd/MM/yyyy")
        val date = format.parse(format.format(Date()))

        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, -30)
        val iDate = calendar.time.time

        val entries = arrayListOf<Entry>()
        for (elo in elos) {
            var x = ((elo.date?.time!! - iDate) / (60 * 60 * 24 * 1000))
//            val date = format.parse(format.format(Date(x)))
//            x = date.time
            entries.add(Entry(x.toFloat(), elo.elo.toFloat()))
        }
        val line = LineDataSet(entries, "")
        line.color = ContextCompat.getColor(activity!!, R.color.colorAccent)
        val lines = arrayListOf<ILineDataSet>()
        lines.add(line)
        var data = LineData(lines)
        lineChart.data = data
        lineChart.invalidate()

    }

}