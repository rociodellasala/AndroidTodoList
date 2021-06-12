package com.example.pam_project.features.lists.list;

import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;

public class ListViewHolder extends RecyclerView.ViewHolder {
    private OnListClickedListener listener;

    public ListViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void bind(final ListInformation list) {
        final TextView title = itemView.findViewById(R.id.title);
        final TextView tasksInformation = itemView.findViewById(R.id.number_of_tasks);
        title.setText(list.getTitle());
        String numberOfTasksPending = itemView.getContext().getResources().getString(R.string.list_task_pending);
        String numberOfTasksUrgent = itemView.getContext().getResources().getString(R.string.list_task_urgent);
        String tasksUrgentText = numberOfTasksUrgent + ": " + list.getUrgentPendingTaskCount();
        String tasksPendingText = numberOfTasksPending + ": " + list.getPendingTaskCount();
        String tasksInformationText = tasksUrgentText + " | " + tasksPendingText;
        tasksInformation.setText(tasksInformationText);
        final LinearLayout linearLayout = itemView.findViewById(R.id.listLeftLayout);
        GradientDrawable drawable = (GradientDrawable) linearLayout.getBackground();
        drawable.setColor(list.getColor().getARGBValue());

        final ImageView urgency = itemView.findViewById(R.id.listUrgency);
        if (list.hasUrgentTask()) {
            urgency.setVisibility(View.VISIBLE);
        } else {
            urgency.setVisibility(View.GONE);
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