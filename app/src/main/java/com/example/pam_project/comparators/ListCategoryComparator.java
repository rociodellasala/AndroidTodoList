package com.example.pam_project.comparators;

import com.example.pam_project.features.categories.list.CategoryInformation;
import com.example.pam_project.features.lists.list.ListInformation;

import java.util.Comparator;
import java.util.Objects;

public class ListCategoryComparator extends ListInformationComparator {
    private final Comparator<CategoryInformation> comparator;

    public ListCategoryComparator(final Comparator<CategoryInformation> comparator) {
        Objects.requireNonNull(comparator);

        this.comparator = comparator;
    }

    @Override
    public int compare(ListInformation o1, ListInformation o2) {
        if (o1 == o2)
            return 0;

        int nullCmp = nullCompare(o1, o2);
        if (nullCmp != 0)
            return nullCmp;

        return comparator.compare(o1.getCategory(), o2.getCategory());
    }
}
