import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class CenterZoomLayoutManager(
    context: Context,
    orientation: Int,
    reverseLayout: Boolean = false
) : LinearLayoutManager(context, orientation, reverseLayout) {

    private val shrinkAmount = 0.1f
    private val shrinkDistance = 1.9f

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        // TODO: A small jerk at the start with the animation
        if (true) {
            return super.scrollHorizontallyBy(dx, recycler, state)
        }
        if (orientation == HORIZONTAL) {
            val scrolled = super.scrollHorizontallyBy(dx, recycler, state)
            val midpoint = width / 2.0f
            val d0 = 0.0f
            val d1 = shrinkDistance * midpoint
            val s0 = 1.0f
            val s1 = 1.0f - shrinkAmount
            for (i in 0 until childCount) {
                val child = getChildAt(i) ?: continue
                val childMidpoint = (getDecoratedRight(child) + getDecoratedLeft(child)) / 2.0f
                val d =
                    d1.coerceAtMost(abs(midpoint - childMidpoint)) / d1
                val scale = s0 + (s1 - s0) * (1 - d)
                child.scaleX = scale
                child.scaleY = scale
            }
            return scrolled
        } else {
            return 0
        }
    }

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        if (orientation == VERTICAL) {
            val scrolled = super.scrollVerticallyBy(dy, recycler, state)
            val midpoint = height / 2.0f
            val d0 = 0.0f
            val d1 = shrinkDistance * midpoint
            val s0 = 1.0f
            val s1 = 1.0f - shrinkAmount
            for (i in 0 until childCount) {
                val child = getChildAt(i) ?: continue
                val childMidpoint = (getDecoratedBottom(child) + getDecoratedTop(child)) / 2.0f
                val d =
                    d1.coerceAtMost(abs(midpoint - childMidpoint)) / d1
                val scale = s0 + (s1 - s0) * (1 - d)
                child.scaleX = scale
                child.scaleY = scale
            }
            return scrolled
        } else {
            return 0
        }
    }

//    override fun smoothScrollToPosition(
//        recyclerView: RecyclerView,
//        state: RecyclerView.State?,
//        position: Int
//    ) {
//        val smoothScroller =
//            object : androidx.recyclerview.widget.LinearSmoothScroller(recyclerView.context) {
//                override fun getHorizontalSnapPreference(): Int {
//                    return androidx.recyclerview.widget.LinearSmoothScroller.SNAP_TO_START
//                }
//            }
//        smoothScroller.targetPosition = position
//        startSmoothScroll(smoothScroller)
//    }

    companion object {
        const val HORIZONTAL = RecyclerView.HORIZONTAL
        const val VERTICAL = RecyclerView.VERTICAL
    }
}
