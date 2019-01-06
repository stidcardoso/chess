package com.teda.chesstactics.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.teda.chesstactics.R
import kotlinx.android.synthetic.main.fragment_chessboard.*

class ChessBoardFragment : Fragment() {

    companion object {
        fun newInstance(): ChessBoardFragment {
            return ChessBoardFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chessboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var layoutManager = GridLayoutManager(activity, 8)
//        recyclerChess.layoutManager = layoutManager
//        recyclerChess.adapter = ChessBoardAdapter()
//        chessPieces.setFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR")
    }

}