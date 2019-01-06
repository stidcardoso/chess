package com.teda.chesstactics.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.teda.chesstactics.R

class ChessBoardAdapter : RecyclerView.Adapter<ChessBoardViewHolder>() {

    var evenFile: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChessBoardViewHolder {
        return ChessBoardViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_square, parent, false))
    }

    override fun getItemCount(): Int = 64

    override fun onBindViewHolder(holder: ChessBoardViewHolder, position: Int) {
        if ((position % 8) == 0) {
            evenFile = !evenFile
        }
        holder.bind(evenFile)
    }

}