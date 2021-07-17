package com.example.pam_project.features.tasks.list

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.pam_project.R

class TaskViewHolderPending(itemView: View) : TaskViewHolder(itemView) {
    override fun bind(task: TaskInformation?) {
        super.bind(task)
        val description = itemView.findViewById<TextView>(R.id.description)
        description.text = task?.description
        val urgency = itemView.findViewById<ImageView>(R.id.urgency)
        if (task?.urgency == true) {
            urgency.visibility = View.VISIBLE
        } else {
            urgency.visibility = View.GONE
        }
    }
}