package com.teda.chesstactics.ui

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
import com.teda.chesstactics.R
import com.teda.chesstactics.Utilities
import com.teda.chesstactics.data.entity.Elo
import com.teda.chesstactics.data.entity.Position
import com.teda.chesstactics.ui.chess.ChessPieces
import com.teda.chesstactics.ui.viewmodel.PositionViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


class HomeFragment : Fragment(), ChessPieces.ChessCallback {

    companion object {
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        positionViewModel = ViewModelProviders.of(this).get(PositionViewModel::class.java)
        positionViewModel.getPosition().observe(this, Observer { position ->
            position?.let {
                calculateElo = true
                currentPosition = it
                startProblem(it)
            } ?: run {
                //                Log.d("1234", "1234")
                positionViewModel.resetLastSolution()
//                positionViewModel.getNewPosition(1500, 2000)
            }
        })
        positionViewModel.elo?.observe(this, Observer {
            currentElo = it
            textElo.text = currentElo?.elo?.toInt().toString()
        })
/*//        val pieces = Utilities.getPieces("3r1rk1/ppp2ppp/2qb1n2/6Rb/3p4/N2B1PB1/PPP3PP/R1Q4K")
//        val pieces = Utilities.getPieces("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR")
//        chessPieces.setChessPieces(pieces)*/
        chessPieces.setChessCallbackListener(this)
        imageRetry.setOnClickListener {
            //            startProblem()
            positionViewModel.getNewPosition(1500, 2000)
            chronometer.base = SystemClock.elapsedRealtime() + timeStopped
            chronometer.start()
        }
        imageNext.setOnClickListener {
            problemStarted = false
            positionViewModel.getNewPosition(1500, 2000)
        }
    }

    private fun startProblem(position: Position) {
        groupResult.visibility = View.GONE
        cardView2.visibility = View.VISIBLE
        if (problemStarted)
            chessPieces.retryProblem()
        else {
            chessPieces.setChessProblem(position)
            problemStarted = true
        }
    }

/*
    fun problem(): Position {
        var problem = Position()
        problem.whiteToPlay = true
        problem.initialPosition = "2k5/ppp4p/2n5/3N2B1/3P4/2n2PPK/P1r5/4R3"
        problem.pgn = "30. Re8+ Kd7 31. Nf6+ Kd6 32. Bf4+"
        problem.setMovements()
//        var movements = arrayListOf<Movement>(Movement("d3","b5"))
//        var answers = arrayListOf<Movement>()
//        problem.solutions
        return problem
    }
*/

    private fun animateColor(newColor: Int) {
        val colorFrom = ContextCompat.getColor(activity!!, R.color.transparent)
        val colorTo = ContextCompat.getColor(activity!!, newColor)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 400 // milliseconds
        colorAnimation.addUpdateListener {
            imageBackgroundResult.setBackgroundColor(it.animatedValue as Int)
        }
        colorAnimation.start()
    }

    private fun stopTimer() {
        timeStopped = chronometer.base - SystemClock.elapsedRealtime()
        chronometer.stop()
    }

    override fun onMoveError() {
        if (calculateElo) {
            calculateElo = false
            val elo = Utilities.calculateNewElo(currentElo!!, currentPosition!!.elo.toDouble(), 0.0)
            positionViewModel.insertOrUpdateElo(elo)
        }
        groupResult.visibility = View.VISIBLE
        cardView2.visibility = View.INVISIBLE
        stopTimer()
        imageResult.setImageResource(R.drawable.ic_close_24dp)
        animateColor(R.color.colorAccent)
    }

    override fun onProblemSolved() {
        if (calculateElo) {
            calculateElo = false
            val elo = Utilities.calculateNewElo(currentElo!!, currentPosition!!.elo.toDouble(), 1.0)
            positionViewModel.insertOrUpdateElo(elo)
        }
        currentPosition?.lastSolution = Date()
        positionViewModel.updatePosition(currentPosition!!)
        animateColor(R.color.greenSuccess)
        groupResult.visibility = View.VISIBLE
        cardView2.visibility = View.INVISIBLE
        chronometer.stop()
        imageResult.setImageResource(R.drawable.ic_check_24dp)
    }

}