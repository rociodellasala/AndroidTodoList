package com.example.pam_project.features.lists.list

import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pam_project.R

class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var listener: OnListClickedListener? = null
    fun bind(list: ListInformation?) {
        val title = itemView.findViewById<TextView>(R.id.title)
        val tasksInformation = itemView.findViewById<TextView>(R.id.number_of_tasks)
        title.text = list?.title
        val numberOfTasksPending = itemView.context.resources.getString(R.string.list_task_pending)
        val numberOfTasksUrgent = itemView.context.resources.getString(R.string.list_task_urgent)
        val tasksUrgentText = numberOfTasksUrgent + ": " + list?.urgentPendingTaskCount
        val tasksPendingText = numberOfTasksPending + ": " + list?.pendingTaskCount
        val tasksInformationText = "$tasksUrgentText | $tasksPendingText"
        tasksInformation.text = tasksInformationText
        val linearLayout = itemView.findViewById<LinearLayout>(R.id.listLeftLayout)
        val drawable = linearLayout.background as GradientDrawable
        drawable.setColor(list?.color?.aRGBValue!!)
        val urgency = itemView.findViewById<ImageView>(R.id.listUrgency)
        if (list.hasUrgentTask()) {
            urgency.visibility = View.VISIBLE
        } else {
            urgency.visibility = View.GONE
        }
        itemView.setOnClickListener { v: View? ->
            if (listener != null) {
                listener!!.onClick(list.id)
            }
        }
    }

    fun setOnClickListener(listener: OnListClickedListener?) {
        this.listener = listener
    }
}