package kr.ac.jbnu.ch.frameworks.models

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ListViewDecoration(private val divHeight : Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        if(parent.getChildAdapterPosition(view) != parent.adapter?.itemCount?.minus(1) ?: 0){
            outRect.bottom = divHeight
        }
    }
}