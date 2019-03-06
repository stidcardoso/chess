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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.teda.chesstactics.App
import com.teda.chesstactics.Constants
import com.teda.chesstactics.R
import com.teda.chesstactics.data.entity.GroupPositions
import com.teda.chesstactics.data.entity.Position
import com.teda.chesstactics.ui.chess.ChessPieces
import com.teda.chesstactics.ui.viewmodel.GroupListViewModel
import kotlinx.android.synthetic.main.activity_position.*

class PositionFragment : Fragment(), ChessPieces.ChessCallback {

    companion object {
        fun newInstance(position: Int): PositionFragment {
            val fragment = PositionFragment()
            val args = Bundle()
            args.putInt(Constants.EXTRAS_POSITION, position)
            fragment.arguments = args
            return fragment
        }
    }

    private var groupListViewModel: GroupListViewModel? = null
    private var problemStarted = false
    private var currentPosition = 0
    private var positions: List<Position>? = null
    private var timeStopped: Long = 0
    private var nextPuzzleAutomatically = false
    var goToNextPuzzle = false
//    private lateinit var preferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.activity_position, container, false)
        currentPosition = arguments!!.getInt(Constants.EXTRAS_POSITION, 0)
        return v
//        val position = intent.extras.getSerializable(Constants.EXTRAS_POSITION) as Position
//        position.setMovements()
//        startProblem(position)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        chessPieces.setChessCallbackListener(this)
        groupListViewModel = activity?.run {
            ViewModelProviders.of(this).get(GroupListViewModel::class.java)
        } ?: throw Exception("Invalid activity")
        val observer = Observer<GroupPositions?> {
            positions = it?.positions
            startProblem(it?.positions!![currentPosition])
            groupListViewModel?.group?.removeObservers(this)
        }
        groupListViewModel?.group?.observe(this, observer)

        imagePrevious.setOnClickListener {
            goToNextPuzzle = false
            goPreviousPuzzle()
        }

        imageNext.setOnClickListener {
            goToNextPuzzle = false
            goNextPuzzle()
        }

        imageRetry.setOnClickListener {
            resumeTime()
            restartProblem()
        }

        imageHint.setOnClickListener {
            chessPieces.showHighlight()
        }
    }

    private fun goPreviousPuzzle() {
        restartTime()
        if (currentPosition > 0) {
            currentPosition -= 1
            groupListViewModel?.setCurrentPosition(currentPosition)
            updateKingIcon()
            startProblem(positions!![currentPosition])
        }
    }

    private fun goNextPuzzle() {
        restartTime()
        if (currentPosition < positions!!.size - 1) {
            currentPosition += 1
            groupListViewModel?.setCurrentPosition(currentPosition)
            updateKingIcon()
            startProblem(positions!![currentPosition])
        }
    }

    private fun updateKingIcon() {
        if (!positions!![currentPosition].whiteToPlay)
            imagePlay.setImageResource(R.drawable.ic_bking)
        else
            imagePlay.setImageResource(R.drawable.ic_wking)
    }

    override fun onResume() {
        super.onResume()
        resumeTime()
        if (App.prefs!!.getBoolean("keyScreenOn", false))
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        nextPuzzleAutomatically = App.prefs!!.getBoolean(Constants.KEY_GO_TO_NEXT_PUZZLE, false)
        chessPieces.sound = App.prefs!!.getBoolean(Constants.KEY_SOUND, false)
    }

    override fun onPause() {
        super.onPause()
        stopTimer()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun startProblem(position: Position) {
        groupResult.visibility = View.GONE
//        cardView2.visibility = View.VISIBLE
        /*if (problemStarted)
            chessPieces.retryProblem()
        else {*/
        resumeTime()
        val copyPosition = Position()
        copyPosition.copy(position)
        chessPieces.setChessProblem(copyPosition)
        problemStarted = true
    }

    private fun restartProblem() {
        groupResult.visibility = View.GONE
        chessPieces.retryProblem()
    }

    override fun onMoveError() {
        groupResult.visibility = View.VISIBLE
        cardView2.visibility = View.INVISIBLE
        stopTimer()
        imageResult.setImageResource(R.drawable.ic_close_24dp)
        animateColor(R.color.colorAccent)
    }

    override fun onProblemSolved() {
        groupResult.visibility = View.VISIBLE
        cardView2.visibility = View.INVISIBLE
        stopTimer()
        imageResult.setImageResource(R.drawable.ic_check_24dp)
        if (nextPuzzleAutomatically)
            goToNextPuzzle = true
        animateColor(R.color.greenSuccess)
        positions?.get(currentPosition)?.groupSolved = true
        groupListViewModel?.updatePosition(positions!![currentPosition])
    }

    private fun animateColor(newColor: Int) {
        val colorFrom = ContextCompat.getColor(activity!!, R.color.transparent)
        val colorTo = ContextCompat.getColor(activity!!, newColor)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 700 // milliseconds
        colorAnimation.addUpdateListener {
            imageBackgroundResult.setBackgroundColor(it.animatedValue as Int)
        }
        colorAnimation.addListener(endListener)
        colorAnimation.start()
    }

    private fun resumeTime() {
        chronometer.base = SystemClock.elapsedRealtime() + timeStopped
        chronometer.start()
    }

    private fun restartTime() {
        timeStopped = 0
        chronometer.start()
    }

    private fun stopTimer() {
        timeStopped = chronometer.base - SystemClock.elapsedRealtime()
        chronometer.stop()
    }

    private val endListener = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)
            if (goToNextPuzzle)
                goNextPuzzle()
        }
    }

}