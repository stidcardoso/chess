package com.teda.chesstactics.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.teda.chesstactics.Constants
import com.teda.chesstactics.R
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
        groupListViewModel = activity?.run {
            ViewModelProviders.of(this).get(GroupListViewModel::class.java)
        } ?: throw Exception("Invalid activity")

        groupListViewModel?.group?.observe(this, Observer {
            positions = it?.positions
            startProblem(it?.positions!![currentPosition])
        })

        imagePrevious.setOnClickListener {
            if (currentPosition > 0) {
                currentPosition -= 1
                groupListViewModel?.setCurrentPosition(currentPosition)
                chessPieces.setChessProblem(positions!![currentPosition])
            }
        }

        imageNext.setOnClickListener {
            if (currentPosition < positions!!.size - 1) {
                currentPosition += 1
                groupListViewModel?.setCurrentPosition(currentPosition)
                chessPieces.setChessProblem(positions!![currentPosition])
            }
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

    override fun onMoveError() {

    }

    override fun onProblemSolved() {

    }

}