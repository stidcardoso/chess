package com.teda.chesstactics.ui.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import com.teda.chesstactics.Constants
import com.teda.chesstactics.data.entity.Group
import com.teda.chesstactics.ui.GroupListActivity
import kotlinx.android.synthetic.main.item.view.*

class MViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    lateinit var group: Group

    init {
        itemView.textGroupName.setOnClickListener {
            val i = Intent(view.context, GroupListActivity::class.java)
            i.putExtra(Constants.EXTRAS_GROUP_ID, group.id)
            view.context.startActivity(i)
        }
    }

    fun bind(group: Group) {
        this.group = group
        itemView.textGroupName.text = group.name
    }

}