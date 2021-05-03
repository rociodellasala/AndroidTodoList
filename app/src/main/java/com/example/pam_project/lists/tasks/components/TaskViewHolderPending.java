package com.example.pam_project.lists.tasks.components;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;

public class TaskViewHolderPending extends RecyclerView.ViewHolder {
    public TaskViewHolderPending(@NonNull View itemView) {
        super(itemView);
    }

    public void bind(final TaskInformation task) {
        final TextView title = itemView.findViewById(R.id.title);
        final TextView description = itemView.findViewById(R.id.description);
        title.setText(task.getTitle());
        description.setText(task.getDescription());
        if (task.getUrgency()) {
            final ImageView urgency = itemView.findViewById(R.id.urgency);
            urgency.setVisibility(View.VISIBLE);
        }
    }
}