package com.teda.chesstactics.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.teda.chesstactics.R
import com.teda.chesstactics.data.entity.Position
import io.reactivex.subjects.PublishSubject

class NumberAdapter(var positions: List<Position>) : RecyclerView.Adapter<NumberViewHolder>() {

    val clickSubject = PublishSubject.create<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_number, parent, false)
        return NumberViewHolder(v, clickSubject)
    }

    override fun getItemCount(): Int {
        return positions.size
    }

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
        holder.bind(positions[position])
    }

}