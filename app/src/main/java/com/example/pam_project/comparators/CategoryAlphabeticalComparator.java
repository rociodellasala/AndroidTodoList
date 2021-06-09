package com.example.pam_project.comparators;

import com.example.pam_project.features.categories.list.CategoryInformation;

public class CategoryAlphabeticalComparator extends CategoryInformationComparator {

    @Override
    public int compare(CategoryInformation o1, CategoryInformation o2) {
        return String.CASE_INSENSITIVE_ORDER.compare(o1.getTitle(), o2.getTitle());
    }
}
