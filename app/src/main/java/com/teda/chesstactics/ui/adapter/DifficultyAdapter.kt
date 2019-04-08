package com.teda.chesstactics.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.teda.chesstactics.R
import com.teda.chesstactics.data.entity.Position
import io.reactivex.subjects.PublishSubject

class DifficultyAdapter(var positions: List<Position>) : RecyclerView.Adapter<DifficultyViewHolder>() {

    val clickSubject = PublishSubject.create<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DifficultyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_number, parent, false)
        return DifficultyViewHolder(v, clickSubject)
    }

    override fun getItemCount(): Int {
        return positions.size
    }

    override fun onBindViewHolder(holder: DifficultyViewHolder, position: Int) {
        holder.bind(positions[position])
    }

}