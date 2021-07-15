package com.example.pam_project.features.tasks.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pam_project.R
import com.example.pam_project.features.lists.list.OnListClickedListener
import com.example.pam_project.utils.constants.TaskStatus
import java.util.*

class TaskAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listener: OnListClickedListener? = null
    private val dataSet: MutableList<TaskInformation?>
    fun update(newDataSet: List<TaskInformation?>?) {
        dataSet.clear()
        if (newDataSet != null) {
            dataSet.addAll(newDataSet)
        }
        notifyDataSetChanged()
    }

    // Sabemos que no deberiamos hacer un notifyDataSetChanged al final
    // pero por alguna razon nos rompe y no pudimos encontrarlo
    fun update(modifiedTask: TaskInformation?, index: Int) {
        dataSet.add(modifiedTask)
        notifyItemInserted(dataSet.indexOf(modifiedTask))
        dataSet.removeAt(index)
        notifyItemRemoved(index)
        notifyDataSetChanged()
    }

    fun setOnClickedListener(listener: OnListClickedListener?) {
        this.listener = listener
    }

    fun getItem(adapterPosition: Int): TaskInformation? {
        return dataSet[adapterPosition]
    }

    fun areAllComplete(): Boolean {
        for (task in dataSet) {
            if (task!!.status == TaskStatus.PENDING) return false
        }
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == TASK_PENDING) {
            view = layoutInflater.inflate(R.layout.task_view_holder_pending, parent, false)
            TaskViewHolderPending(view)
        } else {
            view = layoutInflater.inflate(R.layout.task_view_holder_done, parent, false)
            TaskViewHolderDone(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataSet[position]
        if (holder is TaskViewHolderPending) {
            holder.bind(item)
            holder.setOnClickListener(listener)
        } else {
            (holder as TaskViewHolderDone).bind(item)
            holder.setOnClickListener(listener)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getItemViewType(position: Int): Int {
        val status = dataSet[position]!!.status
        return if (status == TaskStatus.PENDING) TASK_PENDING else TASK_DONE
    }

    companion object {
        private const val TASK_PENDING = 0
        private const val TASK_DONE = 1
    }

    init {
        dataSet = ArrayList()
    }
}