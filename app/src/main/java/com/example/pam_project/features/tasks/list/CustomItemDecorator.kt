package com.example.pam_project.features.tasks.list

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.pam_project.R
import java.util.*

class CustomItemDecorator(context: Context, @LayoutRes resId: Int, private val allHeaders: Array<String>) : ItemDecoration() {
    private val mLayout: Array<View?>
    private val textView: Array<TextView?>
    private val context: Context
    private val resId: Int
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        var currentViewType = -1
        var i = 0
        var j = 0
        while (i < parent.childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
            val viewType = Objects.requireNonNull(parent.adapter).getItemViewType(position)
            if (viewType != currentViewType && j < allHeaders.size) {
                mLayout[j]!!.layout(parent.left, 0, parent.right, mLayout[j]!!.measuredHeight)
                textView[j]!!.visibility = View.VISIBLE
                textView[j]!!.y = (view.top - mLayout[j]!!.measuredHeight).toFloat()
                mLayout[j]!!.draw(c)
                currentViewType = viewType
                j++
            }
            i++
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val headers = updatePendingHeaders(parent)
        updateLayout(parent, headers)
        var currentViewType = -1
        var i = 0
        var j = 0
        while (i < parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)
            val viewType = Objects.requireNonNull(parent.adapter).getItemViewType(position)
            if (viewType != currentViewType && j < headers.size) {
                outRect[0, mLayout[j]!!.measuredHeight, 0] = 0
                currentViewType = viewType
                j++
            } else {
                outRect.setEmpty()
            }
            i++
        }
    }

    private fun updateLayout(parent: RecyclerView, headers: List<String>) {
        for (i in headers.indices) {
            mLayout[i] = LayoutInflater.from(context).inflate(resId, parent, false)
            textView[i] = mLayout[i].findViewById(R.id.recycler_header)
            textView[i].setText(headers[i])
            textView[i].setVisibility(View.INVISIBLE)
            mLayout[i].measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        }
    }

    private fun updatePendingHeaders(parent: RecyclerView): List<String> {
        val updatedHeaders: MutableList<String> = ArrayList()
        var currentViewType = -1
        for (i in 0 until parent.childCount) {
            val view1 = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view1)
            val viewType = Objects.requireNonNull(parent.adapter).getItemViewType(position)
            if (viewType != currentViewType) {
                if (viewType == TASK_PENDING) {
                    if (!updatedHeaders.contains(allHeaders[TASK_PENDING])) updatedHeaders.add(allHeaders[TASK_PENDING])
                } else {
                    if (!updatedHeaders.contains(allHeaders[TASK_DONE])) updatedHeaders.add(allHeaders[TASK_DONE])
                }
                currentViewType = viewType
            }
        }
        return updatedHeaders
    }

    companion object {
        private const val TASK_PENDING = 0
        private const val TASK_DONE = 1
    }

    init {
        mLayout = arrayOfNulls(allHeaders.size)
        textView = arrayOfNulls(allHeaders.size)
        this.context = context
        this.resId = resId
    }
}