package com.example.pam_project.lists.lists.components.comparators;

import com.example.pam_project.lists.categories.components.CategoryInformation;

import java.util.Comparator;

public abstract class CategoryInformationComparator implements Comparator<CategoryInformation> {

    public int nullCompare(Object o1, Object o2) {
        if (o2 == null)
            return Integer.MIN_VALUE;
        if (o1 == null)
            return Integer.MAX_VALUE;
        return 0;
    }
}
