package com.teda.chesstactics.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.teda.chesstactics.R
import com.teda.chesstactics.data.entity.Group

class BuyGroupAdapter(var list: ArrayList<Group>) : RecyclerView.Adapter<BuyGroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyGroupViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_buy_package, parent, false)
        return BuyGroupViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: BuyGroupViewHolder, position: Int) {
        viewHolder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}