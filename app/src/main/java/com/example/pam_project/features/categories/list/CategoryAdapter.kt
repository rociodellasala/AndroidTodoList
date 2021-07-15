package com.example.pam_project.features.categories.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pam_project.R
import com.example.pam_project.features.lists.list.OnListClickedListener
import java.util.*

class CategoryAdapter : RecyclerView.Adapter<CategoryViewHolder>() {
    private val dataSet: MutableList<CategoryInformation?>
    private var listener: OnListClickedListener? = null
    fun update(newDataSet: List<CategoryInformation?>?) {
        dataSet.clear()
        if (newDataSet != null) {
            dataSet.addAll(newDataSet)
        }
        notifyDataSetChanged()
    }

    fun setOnClickedListener(listener: OnListClickedListener?) {
        this.listener = listener
    }

    fun swapItems(draggedPosition: Int, targetPosition: Int) {
        Collections.swap(dataSet, draggedPosition, targetPosition)
        notifyItemMoved(draggedPosition, targetPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.category_view_holder, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(dataSet[position])
        holder.setOnClickListener(listener)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    init {
        dataSet = ArrayList()
    }
}