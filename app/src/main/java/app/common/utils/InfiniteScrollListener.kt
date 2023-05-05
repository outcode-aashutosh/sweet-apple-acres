package app.common.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


abstract class InfiniteScrollListener(private val VISIBLE_THRESHOLD: Int = 5) :
    RecyclerView.OnScrollListener() {

    private var previousTotal = 0
    private var loading = true
    private var visibleThreshold = VISIBLE_THRESHOLD
    private var firstVisibleItem = 0
    private var lastVisibleItem = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    companion object {
        private const val TAG = "InfiniteScrollListener"
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy > 0) {

            val layoutManager = recyclerView.layoutManager
            totalItemCount = layoutManager?.itemCount ?: 0
            visibleItemCount = recyclerView.childCount
            when (layoutManager) {
                is StaggeredGridLayoutManager -> {
                    visibleThreshold = VISIBLE_THRESHOLD * layoutManager.spanCount
                    firstVisibleItem = layoutManager.findFirstVisibleItemPositions(null)[0]
                    val lastVisibleItemPositions = layoutManager.findLastVisibleItemPositions(null)
                    lastVisibleItem = getLastVisibleItem(lastVisibleItemPositions)
                }
                is GridLayoutManager -> {
                    visibleThreshold = VISIBLE_THRESHOLD * layoutManager.spanCount
                    firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                }
                is LinearLayoutManager -> {
                    firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                }
            }

            if (loading && totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }

            if (!loading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)
            ) {
                // End has been reached
                onLoadMore(totalItemCount, recyclerView)
                loading = true
            }
        }
    }

    //call when data is reloaded
    fun resetState() {
        this.totalItemCount = 0
        this.previousTotal = 0
    }

    // Defines the process for actually loading more data based on total item count
    abstract fun onLoadMore(totalItemsCount: Int, view: RecyclerView)
}