package com.example.pam_project.lists.lists.components.comparators;

import com.example.pam_project.lists.lists.components.ListInformation;

import java.util.Comparator;

public abstract class ListInformationComparator implements Comparator<ListInformation> {

    public int nullCompare(ListInformation o1, ListInformation o2) {
        if (o2 == null)
            return Integer.MIN_VALUE;
        if (o1 == null)
            return Integer.MAX_VALUE;
        return 0;
    }
}
