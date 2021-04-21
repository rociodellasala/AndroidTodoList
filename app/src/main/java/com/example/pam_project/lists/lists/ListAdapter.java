package com.example.pam_project.lists.lists;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;
import com.example.pam_project.db.entities.ListEntity;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {
    private final List<ListInformation> dataSet;
    private List<Integer> filterSelections;
    private OnListClickedListener listener;
    private int sortIndex = 0;

    public ListAdapter(List<ListInformation> dataSet) {
        this.dataSet = dataSet;
    }

    public void setOnClickedListener(final OnListClickedListener listener) {
        this.listener = listener;
    }

    public void addItem(ListInformation newList) {
        this.dataSet.add(newList);
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_holder, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.bind(dataSet.get(position));
        holder.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
        // TODO: sort dataset accordingly
    }

    public List<Integer> getFilterSelections() {
        return filterSelections;
    }

    public void setFilterSelections(final List<Integer> filterSelections) {
        this.filterSelections = filterSelections;
        // TODO: filter dataset accordingly
    }
}