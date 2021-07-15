package com.example.pam_project.features.categories.list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pam_project.R
import com.example.pam_project.features.lists.list.OnListClickedListener

class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var listener: OnListClickedListener? = null
    fun bind(category: CategoryInformation?) {
        val categoryTitle = itemView.findViewById<TextView>(R.id.category_title)
        categoryTitle.text = category.getTitle()
        itemView.setOnClickListener { v: View? ->
            if (listener != null) {
                listener!!.onClick(category.getId())
            }
        }
    }

    fun setOnClickListener(listener: OnListClickedListener?) {
        this.listener = listener
    }
}