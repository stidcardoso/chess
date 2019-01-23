package com.teda.chesstactics

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View


class GridSpace(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = space
        outRect.right = space
        outRect.bottom = space
        // Add top margin only for the first item to avoid double space between items
        /*if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = space
        }*/
    }
}