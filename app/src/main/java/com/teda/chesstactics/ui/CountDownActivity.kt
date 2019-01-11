package com.teda.chesstactics.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import com.teda.chesstactics.R
import kotlinx.android.synthetic.main.activity_count_down.*
import java.util.concurrent.TimeUnit


class CountDownActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down)
    }

    override fun onStart() {
        super.onStart()
        startCountDown()
    }

    private fun startCountDown() {
        object : CountDownTimer(100000, 1000) {
            override fun onTick(p0: Long) {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(p0)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(p0) -
                        TimeUnit.MINUTES.toSeconds(minutes)
                textCountDown.text = String.format("%02d : %02d", minutes, seconds)
            }

            override fun onFinish() {
            }

        }.start()
    }
}