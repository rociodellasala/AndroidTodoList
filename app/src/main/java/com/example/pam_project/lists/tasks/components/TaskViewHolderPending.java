package com.example.pam_project.lists.tasks.components;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;
import com.example.pam_project.lists.lists.listActivity.OnListClickedListener;

public class TaskViewHolderPending  extends TaskViewHolder {

    public TaskViewHolderPending(@NonNull View itemView) {
        super(itemView);
    }

    public void bind(final TaskInformation task) {
        super.bind(task);
        final TextView description = itemView.findViewById(R.id.description);
        description.setText(task.getDescription());
        if (task.getUrgency()) {
            final ImageView urgency = itemView.findViewById(R.id.urgency);
            urgency.setVisibility(View.VISIBLE);
        }
    }
}