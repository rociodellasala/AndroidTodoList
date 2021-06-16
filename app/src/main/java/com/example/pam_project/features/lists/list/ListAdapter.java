package com.example.pam_project.features.lists.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_project.R;
import com.example.pam_project.features.categories.list.CategoryInformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> implements Filterable {
    private static List<Integer> filterSelections;
    private static int sortIndex = 0;

    private final List<ListInformation> dataSet;
    private final List<ListInformation> hiddenItems;
    private final List<ListInformation> completeDataset;
    private final List<ListInformation> previousSearchDataset;
    private List<ListInformation> searchItems;
    private final Map<Long, CategoryInformation> categoriesWithIds;
    private List<CategoryInformation> categories;
    private OnListClickedListener listener;

    public ListAdapter() {
        this.dataSet = new ArrayList<>();
        this.categoriesWithIds = new HashMap<>();
        this.categories = new ArrayList<>(categoriesWithIds.values());
        this.hiddenItems = new ArrayList<>();
        this.completeDataset = new ArrayList<>();
        this.previousSearchDataset = new ArrayList<>();
        this.searchItems = new ArrayList<>();
    }

    public void update(final List<ListInformation> newDataSet) {
        this.dataSet.clear();

        if (newDataSet != null)
            dataSet.addAll(newDataSet);

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

    public void setCompleteDataset(final List<ListInformation> dataset){
        completeDataset.clear();
        completeDataset.addAll(dataset);
    }

    public void setPreviousSearchDataset(){
        previousSearchDataset.clear();
        previousSearchDataset.addAll(dataSet);
    }

    public List<ListInformation> getPreviousSearchDataset(){
        return new ArrayList<>(previousSearchDataset);
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


        removeDuplicates(dataSet);
        removeDuplicates(hiddenItems);
        sort();

        notifyDataSetChanged();
        setPreviousSearchDataset();
        ListAdapter.filterSelections = newFilterSelections;
    }

    private void sort() {
        Collections.sort(dataSet, ListInformation.getComparator(sortIndex));
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    searchItems = completeDataset;
                } else {
                    List<ListInformation> filteredList = new ArrayList<>();
                    for (ListInformation listInformation : completeDataset) {
                        if (listInformation.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(listInformation);
                        }
                    }

                    searchItems = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = searchItems;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                List<?> result = (List<?>) filterResults.values;
                ArrayList<ListInformation> tempList = new ArrayList<>();
                for (Object object : result) {
                    if (object instanceof ListInformation) {
                        tempList.add((ListInformation) object); // <-- add to temp
                    }
                }

                dataSet.clear();
                dataSet.addAll(tempList);
                notifyDataSetChanged();
            }
        };
    }

    @FunctionalInterface
    interface ListCheck {
        boolean condition(long id);
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

    private void removeDuplicates(List<ListInformation> list){
        List<ListInformation> listNoDuplicates = new ArrayList<>();
        Set<Long> idSet = new HashSet<>();

        for(ListInformation listInformation: list){
            if(! idSet.contains(listInformation.getId())){
                idSet.add(listInformation.getId());
                listNoDuplicates.add(listInformation);
            }
        }

        list.clear();
        list.addAll(listNoDuplicates);
    }
}