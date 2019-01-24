package com.teda.chesstactics.ui.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.teda.chesstactics.R
import com.teda.chesstactics.data.entity.Position
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_number.view.*

class NumberViewHolder(view: View, clickSubject: PublishSubject<Int>) : RecyclerView.ViewHolder(view) {

    lateinit var position: Position

    init {
        itemView.setOnClickListener {
            clickSubject.onNext(adapterPosition)
            /*val i = Intent(itemView.context, PositionActivity::class.java)
            i.putExtra(Constants.EXTRAS_POSITION, position)
            itemView.context.startActivity(i)*/
        }
    }

    fun bind(position: Position) {
        this.position = position
        itemView.textNumber.text = (adapterPosition + 1).toString()
        if (position.groupSolved)
            itemView.cardNumber.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.greenSuccess))
    }

}