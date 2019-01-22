package com.teda.chesstactics.ui

import android.animation.Animator
import android.animation.ObjectAnimator
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.teda.chesstactics.Constants
import com.teda.chesstactics.R
import com.teda.chesstactics.data.model.Result
import com.teda.chesstactics.ui.chess.ChessPieces
import com.teda.chesstactics.ui.viewmodel.CountDownViewModel
import kotlinx.android.synthetic.main.activity_count_down.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


class CountDownActivity : AppCompatActivity(), ChessPieces.ChessCallback {

    private var countPositions = 0
    private var countSuccess = 0
    private var countError = 0
    private var averageElo = 0
    var time = 100
    var millisLeft = 0L
    private var currentTime = 0L
    private lateinit var countDown: CountDownTimer
    private val animation by lazy { ObjectAnimator.ofInt(progressBar, "progress", 1000, 0) }
    private lateinit var countDownViewModel: CountDownViewModel
    lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down)
        mToolbar = toolbar as Toolbar
        mToolbar.setTitle(R.string.count_down)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        time = intent.getIntExtra(Constants.EXTRAS_TIME_COUNT_DOWN, 1) * 60
        countDownViewModel = ViewModelProviders.of(this).get(CountDownViewModel::class.java)
        countDownViewModel.getPosition().observe(this, Observer { position ->
            position?.let {
                it.setMovements()
                chessPieces.setChessProblem(it)
                countPositions += 1
                averageElo = ((averageElo * (countPositions - 1)) + it.elo) / countPositions
            } ?: run {
                countDownViewModel.getNewPosition(1600, 2000)
            }
        })
        countDownViewModel.getNewPosition(1600, 2000)
        chessPieces.setChessCallbackListener(this)
        /*startCountDown()
        animateProgressBar()*/
    }

    override fun onPause() {
        super.onPause()
        currentTime = animation.currentPlayTime
        animation.cancel()
        countDown.cancel()
    }

    override fun onResume() {
        super.onResume()
        animation.duration = time * 1000L
        animation.start()
        animation.currentPlayTime = currentTime
        startCountDown()
    }

    private fun startCountDown() {
        val countTime = if (millisLeft != 0L) millisLeft else time * 1000L
        countDown = object : CountDownTimer(countTime, 1000) {
            override fun onTick(p0: Long) {
                millisLeft = p0
                val minutes = TimeUnit.MILLISECONDS.toMinutes(p0)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(p0) -
                        TimeUnit.MINUTES.toSeconds(minutes)
                textCountDown.text = String.format("%02d:%02d", minutes, seconds)
//                progressBar.progress = ((p0 / 1000).toInt())
            }

            override fun onFinish() {
                val i = Intent(this@CountDownActivity, ResultActivity::class.java)
                i.putExtra(Constants.EXTRAS_RESULT, Result(averageElo.toDouble(), countPositions, countSuccess, countError))
                startActivity(i)
                finish()
            }

        }.start()
    }

    private fun animateProgressBar() {
        animation.duration = time * 1000L
        animation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {}

            override fun onAnimationEnd(animator: Animator) {
            }

            override fun onAnimationCancel(animator: Animator) {}

            override fun onAnimationRepeat(animator: Animator) {}
        })
        animation.start()
    }

    private fun showPositionResult(error: Boolean) {
        var color = 0
        var image = 0
        if (error) {
            color = ContextCompat.getColor(this, R.color.colorAccent)
            image = R.drawable.ic_close_24dp
        } else {
            color = ContextCompat.getColor(this, R.color.greenSuccess)
            image = R.drawable.ic_check_24dp
        }
        imageCircle.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        imageResult.setImageResource(image)
        groupResult.visibility = View.VISIBLE
    }

    override fun onMoveError() {
        showPositionResult(true)
        countError += 1
        thread {
            Thread.sleep(500)
            runOnUiThread {
                groupResult.visibility = View.GONE
                countDownViewModel.getNewPosition(1600, 2000)
            }
        }
    }

    override fun onProblemSolved() {
        showPositionResult(false)
        countSuccess += 1
        thread {
            Thread.sleep(500)
            runOnUiThread {
                groupResult.visibility = View.GONE
                countDownViewModel.getNewPosition(1600, 2000)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}