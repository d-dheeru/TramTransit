package com.gotranspo.tramtransit.ui.products

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.Math.abs

class CenterZoomLayoutManager(context: Context): LinearLayoutManager(context, HORIZONTAL, false) {

    private val scaleDownFactor = 0.8f
    private val distanceToCenterFactor = 0.9f

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {

        val scrolled = super.scrollHorizontallyBy(dx, recycler, state)

//        val midpoint = width / 2f
//        val d1 = 0f
//        val d2 = distanceToCenterFactor * midpoint
//        val s0 = 1f
//        val s1 = 1f - scaleDownFactor
//
//        for (i in 0 until childCount) {
//            val child = getChildAt(i) as RecyclerView
//            val childMidpoint = (getDecoratedRight(child) + getDecoratedLeft(child)) / 2f
//            val d = d2.coerceAtMost(kotlin.math.abs(midpoint - childMidpoint))
//            val scale = s0 - (s0 - s1) * (d - d1) / (d2 - d1)
//            child.scaleX = scale
//            child.scaleY = scale
//        }

        return scrolled
    }
}