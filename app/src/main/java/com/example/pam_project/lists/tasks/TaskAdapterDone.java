package com.example.pam_project.lists.tasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;

import java.util.List;

public class TaskAdapterDone extends RecyclerView.Adapter<TaskViewHolderDone> {
    private final List<TaskInformation> dataSet;

    public TaskAdapterDone(List<TaskInformation> dataSet) {
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public TaskViewHolderDone onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_view_holder_done, parent, false);
        return new TaskViewHolderDone(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolderDone holder, int position) {
        holder.bind(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }
}
