package com.example.pam_project.lists.categories.components;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;
import com.example.pam_project.lists.lists.listActivity.OnListClickedListener;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    private OnListClickedListener listener;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void bind(final CategoryInformation category) {
        final TextView categoryTitle = itemView.findViewById(R.id.category_title);
        categoryTitle.setText(category.getTitle());

        itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(category.getId());
            }
        });
    }

    public void setOnClickListener(final OnListClickedListener listener) {
        this.listener = listener;
    }
}
