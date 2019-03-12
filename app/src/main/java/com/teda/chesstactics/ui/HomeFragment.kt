package com.teda.chesstactics.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.teda.chesstactics.App
import com.teda.chesstactics.Constants
import com.teda.chesstactics.R
import com.teda.chesstactics.Utilities
import com.teda.chesstactics.data.entity.Elo
import com.teda.chesstactics.data.entity.Position
import com.teda.chesstactics.ui.chess.ChessPieces
import com.teda.chesstactics.ui.viewmodel.PositionViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


class HomeFragment() : Fragment(), ChessPieces.ChessCallback {

    companion object {
        const val TAG = "HOME_FRAGMENT"
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    private var timeStopped: Long = 0
    private var problemStarted = false
    private lateinit var positionViewModel: PositionViewModel
    private var currentPosition: Position? = null
    private var currentElo: Elo? = null
    private var calculateElo = true
    private var nextPuzzleAutomatically = false
    var goToNextPuzzle = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        positionViewModel = ViewModelProviders.of(this).get(PositionViewModel::class.java)
        positionViewModel.getPosition().observe(this, Observer { position ->
            position?.let {
                problemStarted = false
                currentPosition = it
                startProblem(it)
                if (position.whiteToPlay)
                    imagePlay.setImageResource(R.drawable.ic_wking)
                else
                    imagePlay.setImageResource(R.drawable.ic_bking)
            } ?: run {
                //                positionViewModel.resetLastSolution()
            }
        })
        positionViewModel.elo?.observe(this, Observer {
            if (currentElo == null)
                positionViewModel.getNewPosition(it)
            currentElo = it
            textElo.text = currentElo?.elo?.toInt().toString()
        })
        chessPieces.setChessCallbackListener(this)
        imageRetry.setOnClickListener {
            groupResult.visibility = View.GONE
            cardView.visibility = View.VISIBLE
            chessPieces.retryProblem()
            resumeTime()
        }
        imageNext.setOnClickListener {
            goToNextPuzzle = false
            if (calculateElo)
                showDialogContinue()
            else
                positionViewModel.getNewPosition(currentElo)
        }
        imageHint.setOnClickListener { _ ->
            if (problemStarted)
                chessPieces.showHighlight()

        }
    }

    override fun onResume() {
        super.onResume()
        resumeTime()
        if (App.prefs!!.getBoolean(Constants.KEY_SCREEN_ON, false))
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        nextPuzzleAutomatically = App.prefs!!.getBoolean(Constants.KEY_GO_TO_NEXT_PUZZLE, false)
        chessPieces.sound = App.prefs!!.getBoolean(Constants.KEY_SOUND, false)
        chessPieces.setFlip(App.prefs!!.getBoolean(Constants.KEY_FLIP_BOARD, true))
    }

    override fun onPause() {
        super.onPause()
        timeStopped = chronometer.base - SystemClock.elapsedRealtime()
        stopTimer()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    fun pause() {
        timeStopped = chronometer.base - SystemClock.elapsedRealtime()
        stopTimer()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    fun resume() {
        resumeTime()
        if (App.prefs!!.getBoolean(Constants.KEY_SCREEN_ON, false))
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        nextPuzzleAutomatically = App.prefs!!.getBoolean(Constants.KEY_GO_TO_NEXT_PUZZLE, false)
        chessPieces.sound = App.prefs!!.getBoolean(Constants.KEY_SOUND, false)
        chessPieces.setFlip(App.prefs!!.getBoolean(Constants.KEY_FLIP_BOARD, true))
    }

    private fun showDialogContinue() {
        val dialog = AlertDialog.Builder(context!!)
        dialog.setTitle(R.string.confirm)
                .setMessage(R.string.desc_quit_exercise)
                .setPositiveButton(
                        getString(android.R.string.ok))
                { _, _ ->
                    stopTimer()
                    calculateNewElo(0.0)
                    currentPosition?.lastSolution = Date()
                    positionViewModel.updatePosition(currentPosition!!)
                    positionViewModel.getNewPosition(currentElo)
                }
                .setNegativeButton(
                        android.R.string.cancel)
                { d, _ ->
                    d.dismiss()
                }
                .show()
    }

    private fun resumeTime() {
        chronometer.base = SystemClock.elapsedRealtime() + timeStopped
        chronometer.start()
    }

    private fun startProblem(position: Position) {
        groupResult.visibility = View.GONE
        cardView.visibility = View.VISIBLE
        calculateElo = true
        chessPieces.setChessProblem(position)
        problemStarted = true
        resumeTime()
    }

    private fun calculateNewElo(result: Double) {
        if (calculateElo) {
            calculateElo = false
            val elo = Utilities.calculateNewElo(currentElo!!, currentPosition!!.elo.toDouble(), result)
            positionViewModel.insertOrUpdateElo(elo)
        }
    }

    private fun animateColor(newColor: Int) {
        val colorFrom = ContextCompat.getColor(activity!!, R.color.transparent)
        val colorTo = ContextCompat.getColor(activity!!, newColor)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 800 // milliseconds
        colorAnimation.addUpdateListener {
            imageBackgroundResult?.setBackgroundColor(it.animatedValue as Int)
        }
        colorAnimation.addListener(endListener)
        colorAnimation.start()
    }

    private fun stopTimer() {
        timeStopped = chronometer.base - SystemClock.elapsedRealtime()
        chronometer.stop()
    }

    override fun onMoveError() {
        problemStarted = false
        calculateNewElo(0.0)
        groupResult.visibility = View.VISIBLE
        cardView.visibility = View.INVISIBLE
        stopTimer()
        imageResult.setImageResource(R.drawable.ic_close_24dp)
        goToNextPuzzle = false
        animateColor(R.color.colorAccent)
    }

    override fun onProblemSolved() {
        problemStarted = false
        calculateNewElo(1.0)
        currentPosition?.lastSolution = Date()
        positionViewModel.updatePosition(currentPosition!!)
        if (nextPuzzleAutomatically)
            goToNextPuzzle = true
        animateColor(R.color.greenSuccess)
        groupResult.visibility = View.VISIBLE
        cardView.visibility = View.INVISIBLE
        stopTimer()
        imageResult.setImageResource(R.drawable.ic_check_24dp)
    }

    private val endListener = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)
            if (goToNextPuzzle)
                positionViewModel.getNewPosition(currentElo)
        }
    }

}