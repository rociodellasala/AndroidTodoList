package com.example.pam_project.lists.lists.components.comparators;

import com.example.pam_project.lists.categories.components.CategoryInformation;

public class CategoryAlphabeticalComparator extends CategoryInformationComparator {

    @Override
    public int compare(CategoryInformation o1, CategoryInformation o2) {
        if (o1 == o2)
            return 0;

        int nullCmp = nullCompare(o1, o2);
        if (nullCmp != 0)
            return nullCmp;

        return o1.getTitle().compareTo(o2.getTitle());
    }
}
