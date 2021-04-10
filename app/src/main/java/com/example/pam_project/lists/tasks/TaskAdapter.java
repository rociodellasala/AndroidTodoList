package com.example.pam_project.lists.tasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    private List<TaskInformation> dataSet;

    public TaskAdapter(List<TaskInformation> dataSet) {
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_view_holder_pending, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }
}
