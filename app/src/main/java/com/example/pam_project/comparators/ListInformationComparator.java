package com.example.pam_project.comparators;

import com.example.pam_project.features.lists.list.ListInformation;

import java.util.Comparator;

public abstract class ListInformationComparator implements Comparator<ListInformation> {

    public int nullCompare(Object o1, Object o2) {
        if (o2 == null)
            return Integer.MIN_VALUE;
        if (o1 == null)
            return Integer.MAX_VALUE;
        return 0;
    }
}
