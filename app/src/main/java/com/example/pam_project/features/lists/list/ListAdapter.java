package com.example.pam_project.features.lists.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;
import com.example.pam_project.features.categories.list.CategoryInformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {
    private static List<Integer> filterSelections;
    private static int sortIndex = 0;

    private final List<ListInformation> dataSet;
    private final List<ListInformation> hiddenItems;
    private final Map<Long, CategoryInformation> categoriesWithIds;
    private List<CategoryInformation> categories;
    private OnListClickedListener listener;

    public ListAdapter() {
        this.dataSet = new ArrayList<>();
        this.categoriesWithIds = new HashMap<>();
        this.categories = new ArrayList<>(categoriesWithIds.values());
        this.hiddenItems = new ArrayList<>();
    }

    public void update(final List<ListInformation> newDataSet) {
        this.dataSet.clear();
        if (newDataSet != null) {
            dataSet.addAll(newDataSet);
        }

        setFilterSelections(filterSelections); // sort and filter accordingly
        notifyDataSetChanged();
    }

    public void updateCategories(final List<CategoryInformation> newCategories) {
        categoriesWithIds.clear();
        if (newCategories != null) {
            for (CategoryInformation category : newCategories) {
                categoriesWithIds.put(category.getId(), category);
            }
        }
        categories = new ArrayList<>(categoriesWithIds.values());

        for (ListInformation list : dataSet) {
            list.setCategory(categoriesWithIds.get(list.getCategoryId()));
        }

        if (filterSelections == null)
            return;

        // reset category selections if one category has been deleted and was selected
        for (int i = 0; i < filterSelections.size(); i++) {
            if (!categoriesWithIds.containsKey(Long.valueOf(filterSelections.get(i)))) {
                filterSelections = null;
                return;
            }
        }
    }

    public void setOnClickedListener(final OnListClickedListener listener) {
        this.listener = listener;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        ListAdapter.sortIndex = sortIndex;
        sort();

        notifyDataSetChanged();
    }

    public List<Integer> getFilterSelections() {
        return filterSelections;
    }

    public List<CategoryInformation> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public void setFilterSelections(final List<Integer> newFilterSelections) {
        if (newFilterSelections == null) {
            sort();
            return;
        }

        final List<Long> selectedCategoriesIds = new ArrayList<>(
                newFilterSelections.size());
        // get ids from selected categories
        for (Integer index : newFilterSelections)
            selectedCategoriesIds.add(categories.get(index).getId());

        // hide items not in selected categories and remove them from dataSet
        moveToList(dataSet, hiddenItems, (id) -> !selectedCategoriesIds.contains(id));

        // show items in selected categories and remove them from hidden items
        moveToList(hiddenItems, dataSet, selectedCategoriesIds::contains);

        sort();

        notifyDataSetChanged();
        ListAdapter.filterSelections = newFilterSelections;
    }

    private void moveToList(List<ListInformation> from, List<ListInformation> to,
                            ListCheck function) {
        // show items in dataSet or hidden categories
        for (ListInformation item : from) {
            if (function.condition(item.getCategoryId())) {
                to.add(item);
            }
        }
        // remove shown items from hidden elements or dataSet
        for (ListInformation item : to)
            from.remove(item);
    }

    private void sort() {
        Collections.sort(dataSet, ListInformation.getComparator(sortIndex));
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_holder, parent, false);
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

    @FunctionalInterface
    interface ListCheck {
        boolean condition(long id);
    }
}