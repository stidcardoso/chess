package com.teda.chesstactics.ui.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import com.teda.chesstactics.Constants
import com.teda.chesstactics.R
import com.teda.chesstactics.data.entity.Group
import com.teda.chesstactics.ui.GroupListActivity
import kotlinx.android.synthetic.main.item_group.view.*

class MViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    lateinit var group: Group

    init {
        itemView.setOnClickListener {
            val i = Intent(view.context, GroupListActivity::class.java)
            i.putExtra(Constants.EXTRAS_GROUP_ID, group.id)
            view.context.startActivity(i)
        }
    }

    fun bind(group: Group) {
        this.group = group
        itemView.textGroupName.text = group.name
        itemView.textPercent.text = group.percentage.toString() + "%"
    }

}