package com.example.pam_project.lists.lists.components;

import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;
import com.example.pam_project.lists.lists.listActivity.OnListClickedListener;

public class ListViewHolder extends RecyclerView.ViewHolder {

    private OnListClickedListener listener;

    public ListViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void bind(final ListInformation list) {
        final TextView title = itemView.findViewById(R.id.title);
        final TextView tasksInformation = itemView.findViewById(R.id.number_of_tasks);
        title.setText(list.getTitle());
        String numberOfTasksTitle = itemView.getContext().getResources().getString(R.string.list_task_quantity);
        String numberOfTasksCompleted = itemView.getContext().getResources().getString(R.string.list_task_completed);
        String tasksQuantityText = numberOfTasksTitle + ": " + list.getTasks().size();
        String tasksCompleteText = numberOfTasksCompleted + ": " + list.getCompletedTasks();
        String tasksInformationText = tasksQuantityText + " | " + tasksCompleteText;
        tasksInformation.setText(tasksInformationText);
        final RelativeLayout relativeLayout = itemView.findViewById(R.id.listLeftLayout);
        GradientDrawable drawable = (GradientDrawable) relativeLayout.getBackground();
        drawable.setColor(list.getColor().getARGBValue());

        if (list.hasUrgentTask()) {
            final ImageView urgency = itemView.findViewById(R.id.listUrgency);
            urgency.setVisibility(View.VISIBLE);
        }

        itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(list.getId());
            }
        });
    }

    public void setOnClickListener(final OnListClickedListener listener) {
        this.listener = listener;
    }
}