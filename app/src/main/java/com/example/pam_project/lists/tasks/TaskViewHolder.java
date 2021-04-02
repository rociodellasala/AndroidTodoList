package com.example.pam_project.lists.tasks;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    public TaskViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void bind(final TaskInformation list) {
        final TextView title = itemView.findViewById(R.id.title);
        final TextView description = itemView.findViewById(R.id.description);
        title.setText(list.getTitle());
        description.setText(list.getDescription());
    }
}
