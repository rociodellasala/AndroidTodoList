package com.example.pam_project.lists.tasks.components;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;
import com.example.pam_project.lists.lists.listActivity.OnListClickedListener;
import com.example.pam_project.utils.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TASK_PENDING = 0;
    private static final int TASK_DONE = 1;
    private OnListClickedListener listener;
    private final List<TaskInformation> dataSet;

    public TaskAdapter() {
        this.dataSet = new ArrayList<>();
    }

    public void update(final List<TaskInformation> newDataSet) {
        this.dataSet.clear();
        if (newDataSet != null) {
            dataSet.addAll(newDataSet);
        }

        notifyDataSetChanged();
    }

    public void update(final TaskInformation modifiedTask, final int index) {
        dataSet.add(modifiedTask);
        notifyItemInserted(dataSet.indexOf(modifiedTask));
        dataSet.remove(index);
        notifyItemRemoved(index);
        notifyDataSetChanged();
    }

    public void setOnClickedListener(final OnListClickedListener listener) {
        this.listener = listener;
    }

    public void addItem(TaskInformation newTask) {
        this.dataSet.add(newTask);
        notifyItemInserted(dataSet.indexOf(newTask));
    }

    public TaskInformation getItem(int adapterPosition) {
        return this.dataSet.get(adapterPosition);
    }

    public boolean areAllComplete(){
        for(TaskInformation task: dataSet){
            if(task.getStatus().equals(TaskStatus.PENDING))
                return false;
        }

        return true;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        if (viewType == TASK_PENDING) {
            view = layoutInflater.inflate(R.layout.task_view_holder_pending, parent, false);
            return new TaskViewHolderPending(view);
        } else {
            view = layoutInflater.inflate(R.layout.task_view_holder_done, parent, false);
            return new TaskViewHolderDone(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TaskInformation item = dataSet.get(position);

        if (holder instanceof TaskViewHolderPending) {
            ((TaskViewHolderPending) holder).bind(item);
            ((TaskViewHolderPending) holder).setOnClickListener(listener);
        } else {
            ((TaskViewHolderDone) holder).bind(item);
            ((TaskViewHolderDone) holder).setOnClickListener(listener);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        TaskStatus status = dataSet.get(position).getStatus();
        return status.equals(TaskStatus.PENDING) ? TASK_PENDING : TASK_DONE;
    }
}