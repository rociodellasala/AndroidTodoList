package com.example.pam_project.features.tasks.list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pam_project.R
import com.example.pam_project.features.lists.list.OnListClickedListener

abstract class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var listener: OnListClickedListener? = null
    open fun bind(task: TaskInformation?) {
        val title = itemView.findViewById<TextView>(R.id.title)
        title.text = task?.title
        itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onClick(task?.id!!)
            }
        }
    }

    fun setOnClickListener(listener: OnListClickedListener?) {
        this.listener = listener
    }
}