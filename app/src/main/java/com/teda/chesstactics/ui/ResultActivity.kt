package com.teda.chesstactics.ui

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.teda.chesstactics.Constants
import com.teda.chesstactics.R
import com.teda.chesstactics.data.model.Result
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    val chartCategories by lazy { arrayOf(getString(R.string.failed), getString(R.string.solved)) }
    lateinit var result: Result
    val colors by lazy {
        arrayListOf<Int>(ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.greenSuccess))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        result = intent.getSerializableExtra(Constants.EXTRAS_RESULT) as Result
        showPie()
        btnContinue.setOnClickListener {
            finish()
        }
    }

    private fun showPie() {
        val results = arrayOf(result.error.toFloat(), result.success.toFloat())
        val pieEntry = ArrayList<PieEntry>()
        pieEntry.add(PieEntry(results[0], chartCategories[0]))
        pieEntry.add(PieEntry(results[1], chartCategories[1]))
        val dataSet = PieDataSet(pieEntry, getString(R.string.results))
        dataSet.colors = colors
        val pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.legend.isEnabled = false
        pieChart.description.isEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.setEntryLabelColor(R.color.white)
        pieChart.invalidate()
    }

}