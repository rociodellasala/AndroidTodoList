package com.example.pam_project.lists.tasks;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;

public class TaskViewHolderDone extends RecyclerView.ViewHolder {
    public TaskViewHolderDone(@NonNull View itemView) {
        super(itemView);
    }

    public void bind(final TaskInformation list) {
        final TextView title = itemView.findViewById(R.id.title);
        title.setText(list.getTitle());
    }
}
