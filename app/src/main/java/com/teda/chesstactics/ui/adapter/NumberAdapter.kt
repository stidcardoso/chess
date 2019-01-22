package com.teda.chesstactics.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.teda.chesstactics.R

class NumberAdapter: RecyclerView.Adapter<NumberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        var v = LayoutInflater.from(parent.context).inflate(R.layout.item, parent)
        return NumberViewHolder(v)
    }

    override fun getItemCount(): Int {

    }

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
    }

}