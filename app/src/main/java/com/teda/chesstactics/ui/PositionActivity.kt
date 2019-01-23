package com.teda.chesstactics.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.teda.chesstactics.Constants
import com.teda.chesstactics.R
import com.teda.chesstactics.data.entity.Position
import com.teda.chesstactics.ui.chess.ChessPieces
import kotlinx.android.synthetic.main.activity_position.*

class PositionActivity : AppCompatActivity(), ChessPieces.ChessCallback {

    private var problemStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_position)
        val position = intent.extras.getSerializable(Constants.EXTRAS_POSITION) as Position
        position.setMovements()
        startProblem(position)
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

    override fun onMoveError() {

    }

    override fun onProblemSolved() {

    }

}