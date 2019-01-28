package com.teda.chesstactics.ui

import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.teda.chesstactics.Constants
import com.teda.chesstactics.R
import com.teda.chesstactics.data.model.Result
import com.teda.chesstactics.util.ValueFormatter
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    val chartCategories by lazy { arrayOf(getString(R.string.failed), getString(R.string.solved)) }
    lateinit var result: Result
    val colors by lazy {
        arrayListOf<Int>(ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.greenSuccess))
    }
    lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        setupToolbar()
        result = intent.getSerializableExtra(Constants.EXTRAS_RESULT) as Result
        showPie()
        textAverage.text = result.averageElo.toString()
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
        pieData.setValueTextColor(Color.WHITE)
        pieData.setValueTextSize(12f)
        pieData.setValueFormatter(ValueFormatter())
        pieChart.data = pieData
        pieChart.legend.isEnabled = false
        pieChart.description.isEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.setEntryLabelColor(R.color.alphaWhite)
        pieChart.invalidate()
    }

    private fun setupToolbar() {
        mToolbar = toolbar as Toolbar
        mToolbar.setTitle(R.string.results)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_24dp)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}