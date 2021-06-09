package com.example.pam_project.features.categories.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;
import com.example.pam_project.features.lists.list.OnListClickedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    private final List<CategoryInformation> dataSet;
    private OnListClickedListener listener;

    public CategoryAdapter() {
        this.dataSet = new ArrayList<>();
    }

    public void update(final List<CategoryInformation> newDataSet) {
        this.dataSet.clear();
        if (newDataSet != null) {
            dataSet.addAll(newDataSet);
        }

        notifyDataSetChanged();
    }

    public void setOnClickedListener(final OnListClickedListener listener) {
        this.listener = listener;
    }

    public List<CategoryInformation> getDataSet() {
        return dataSet;
    }

    public void swapItems(final int draggedPosition, final int targetPosition){
        Collections.swap(dataSet, draggedPosition, targetPosition);
        notifyItemMoved(draggedPosition, targetPosition);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_view_holder, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(dataSet.get(position));
        holder.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
