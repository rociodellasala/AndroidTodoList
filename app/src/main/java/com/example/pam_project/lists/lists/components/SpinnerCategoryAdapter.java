package com.example.pam_project.lists.lists.components;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.pam_project.lists.categories.components.CategoryInformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpinnerCategoryAdapter {
    private final ArrayAdapter<String> categories;
    private final Map<String, Long> categoriesMap;

    public SpinnerCategoryAdapter(Context context, int layout) {
        List<String> categoriesNames = new ArrayList<>();
        categoriesMap = new HashMap<>();
        categories = new ArrayAdapter<>(context, layout, categoriesNames);
    }

    public void update(final List<CategoryInformation> model) {
        for (CategoryInformation categoryInformation : model) {
            categories.add(categoryInformation.getTitle());
            categoriesMap.put(categoryInformation.getTitle(), categoryInformation.getId());
        }
        categories.notifyDataSetChanged();
    }

    public String getCategoryById(final long id) {
        for (String categoryName : categoriesMap.keySet()) {
            Long categoryId = categoriesMap.get(categoryName);
            if (categoryId != null && categoryId.equals(id))
                return categoryName;
        }

        return "";
    }

    public ArrayAdapter<String> getCategories() {
        return categories;
    }

    public Map<String, Long> getCategoriesMap() {
        return categoriesMap;
    }
}
