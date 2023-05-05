package app.common.utils

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EqualSpacingItemDecoration @JvmOverloads constructor(
    spacingDp: Int,
    includeEdgeInGrid: Boolean = true,
    displayMode: Int = -1
) : SpacingItemDecoration(
    spacingDp,
    spacingDp,
    spacingDp,
    spacingDp,
    includeEdgeInGrid,
    displayMode
)

open class SpacingItemDecoration @JvmOverloads constructor(
    private val spacingLeftDp: Int = 0,
    private val spacingTopDp: Int,
    private val spacingRightDp: Int = 0,
    private val spacingBottomDp: Int,
    private var includeEdgeInGrid: Boolean = true,
    private var displayMode: Int = -1
) : RecyclerView.ItemDecoration() {

    //https://gist.github.com/alexfu/f7b8278009f3119f523a
    companion object {
        const val HORIZONTAL = 0
        const val VERTICAL = 1
        const val GRID = 2
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildViewHolder(view).adapterPosition
        val itemCount = state.itemCount
        val layoutManager = parent.layoutManager
        if (layoutManager != null) setSpacingForDirection(
            outRect,
            layoutManager,
            position,
            itemCount
        )
    }

    private fun setSpacingForDirection(
        outRect: Rect,
        layoutManager: RecyclerView.LayoutManager,
        position: Int,
        itemCount: Int
    ) {

        val spacingLeft = spacingLeftDp.dpToPx()
        val spacingTop = spacingTopDp.dpToPx()
        val spacingRight = spacingRightDp.dpToPx()
        val spacingBottom = spacingBottomDp.dpToPx()

        // Resolve display mode automatically
        if (displayMode == -1)
            displayMode = when {
                layoutManager is GridLayoutManager -> GRID
                layoutManager.canScrollHorizontally() -> HORIZONTAL
                else -> VERTICAL
            }

        when (displayMode) {
            HORIZONTAL -> {
                outRect.left = spacingLeft
                outRect.right = if (position == itemCount - 1) spacingRight else 0
                outRect.top = spacingTop
                outRect.bottom = spacingBottom
            }
            VERTICAL -> {
                outRect.left = spacingLeft
                outRect.right = spacingRight
                outRect.top = spacingTop
                outRect.bottom = if (position == itemCount - 1) spacingBottom else 0
            }
            GRID -> if (layoutManager is GridLayoutManager) {
                val spanCount = layoutManager.spanCount
                val column: Int = position % spanCount // item column

                if (includeEdgeInGrid) {
                    outRect.left =
                        spacingLeft - column * spacingLeft / spanCount // spacing - column * ((1f / spanCount) * spacing)
                    outRect.right =
                        (column + 1) * spacingRight / spanCount // (column + 1) * ((1f / spanCount) * spacing)
                    if (position < spanCount) { // top edge
                        outRect.top = spacingTop
                    }
                    outRect.bottom = spacingRight // item bottom
                } else {
                    outRect.left =
                        column * spacingLeft / spanCount // column * ((1f / spanCount) * spacing)
                    outRect.right =
                        spacingRight - (column + 1) * spacingRight / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                    if (position >= spanCount) {
                        outRect.top = spacingTop // item top
                    }
                }
            }
        }
    }

    private fun Int.dpToPx(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }
}