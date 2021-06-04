package com.example.pam_project.features.tasks.list;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;
import com.example.pam_project.features.lists.list.OnListClickedListener;

public abstract class TaskViewHolder extends RecyclerView.ViewHolder {
    private OnListClickedListener listener;

    public TaskViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void bind(final TaskInformation task) {
        final TextView title = itemView.findViewById(R.id.title);
        title.setText(task.getTitle());

        itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(task.getId());
            }
        });
    }

    public void setOnClickListener(final OnListClickedListener listener) {
        this.listener = listener;
    }
}
