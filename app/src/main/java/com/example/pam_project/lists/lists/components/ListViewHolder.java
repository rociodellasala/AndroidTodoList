package com.example.pam_project.lists.lists.components;

import android.graphics.drawable.GradientDrawable;
import android.view.View;
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
        final TextView numberOfTasks = itemView.findViewById(R.id.number_of_tasks);
        title.setText(list.getTitle());
        String numberOfTasksTitle = itemView.getContext().getResources().getString(R.string.list_task_quantity);
        String text = numberOfTasksTitle + ": " + list.getTasks().size();
        numberOfTasks.setText(text);
        GradientDrawable drawable = (GradientDrawable) itemView.getBackground();
        drawable.setColor(list.getColor().getARGBValue());

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