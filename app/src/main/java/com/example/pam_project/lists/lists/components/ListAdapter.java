package com.example.pam_project.lists.lists.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;
import com.example.pam_project.lists.categories.components.CategoryInformation;
import com.example.pam_project.lists.lists.listActivity.OnListClickedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {
    private final List<ListInformation> dataSet;
    private final List<CategoryInformation> categories;
    private final List<ListInformation> hiddenItems;
    private List<Integer> filterSelections;
    private OnListClickedListener listener;
    private int sortIndex = 0;

    public ListAdapter() {
        this.dataSet = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.hiddenItems = new ArrayList<>();
    }

    public void update(final List<ListInformation> newDataSet) {
        this.dataSet.clear();
        if (newDataSet != null) {
            dataSet.addAll(newDataSet);
        }

        notifyDataSetChanged();
    }

    public void updateCategories(final List<CategoryInformation> newCategories) {
        categories.clear();
        if (newCategories != null) {
            categories.addAll(newCategories);
        }
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
        return dataSet.size();
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
        Collections.sort(dataSet, ListInformation.getComparator(sortIndex));

        notifyDataSetChanged();
    }

    public List<Integer> getFilterSelections() {
        return filterSelections;
    }

    public List<CategoryInformation> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public void setFilterSelections(final List<Integer> newFilterSelections) {
        final List<Long> selectedCategoriesIds = new ArrayList<>(
                newFilterSelections.size());
        for (Integer index : newFilterSelections)
            selectedCategoriesIds.add(categories.get(index).getId());

        // hide items not in selected categories
        for (ListInformation shownItem : dataSet) {
            if (!selectedCategoriesIds.contains(shownItem.getCategoryId())) {
                hiddenItems.add(shownItem);
            }
        }
        // remove hidden items from dataSet
        for (ListInformation li : hiddenItems)
            dataSet.remove(li);

        // show items in hidden categories
        for (ListInformation hiddenItem : hiddenItems) {
            if (selectedCategoriesIds.contains(hiddenItem.getCategoryId())) {
                dataSet.add(hiddenItem);
            }
        }
        // remove shown items from hidden elements
        for (ListInformation li : dataSet)
            hiddenItems.remove(li);
        Collections.sort(dataSet, ListInformation.getComparator(sortIndex));

        notifyDataSetChanged();

        this.filterSelections = newFilterSelections;
    }
}