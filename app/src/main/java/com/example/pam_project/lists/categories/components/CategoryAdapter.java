package com.example.pam_project.lists.categories.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;
import com.example.pam_project.lists.lists.components.ListInformation;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder>{
    private final List<CategoryInformation> dataSet;

    public CategoryAdapter() {
        this.dataSet = new ArrayList<>();
    }

    public void update(final List<CategoryInformation> newDataSet){
        this.dataSet.clear();
        if(newDataSet != null){
            dataSet.addAll(newDataSet);
        }

        notifyDataSetChanged();
    }

    public List<CategoryInformation> getDataSet() {
        return dataSet;
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
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }
}
