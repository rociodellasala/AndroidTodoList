package com.example.pam_project.lists.tasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;

import java.util.List;

public class TaskAdapterPending extends RecyclerView.Adapter<TaskViewHolderPending> {
    private List<TaskInformation> dataSet;

    public TaskAdapterPending(List<TaskInformation> dataSet) {
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public TaskViewHolderPending onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_view_holder_pending, parent, false);
        return new TaskViewHolderPending(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolderPending holder, int position) {
        holder.bind(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }
}
