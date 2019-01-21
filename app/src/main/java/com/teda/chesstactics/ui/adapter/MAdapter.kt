package com.teda.chesstactics.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.teda.chesstactics.R

class MAdapter(var list: ArrayList<String>) : RecyclerView.Adapter<MViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return MViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: MViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 10
    }

}