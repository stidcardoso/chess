package com.teda.chesstactics.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.teda.chesstactics.data.entity.Group
import kotlinx.android.synthetic.main.item.view.*

class BuyGroupViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    lateinit var group: Group

    init {
        itemView.setOnClickListener {
        }
    }

    fun bind(group: Group) {
        this.group = group
        itemView.textGroupName.text = group.name
    }

}