package com.teda.chesstactics.ui.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.teda.chesstactics.R
import kotlinx.android.synthetic.main.item_square.view.*

class ChessBoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(evenFile: Boolean) {
        if (evenFile)
            evenFile()
        else
            oddFile()
    }

    fun evenFile() {
        if ((adapterPosition % 2) == 0) {
            itemView.imageSquare.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.colorAccent))
        }
        else
            itemView.imageSquare.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.colorPrimary))
    }

    fun oddFile() {
        if ((adapterPosition % 2) == 0) {
            itemView.imageSquare.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.colorPrimary))
        }
        else
            itemView.imageSquare.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.colorAccent))
    }

}