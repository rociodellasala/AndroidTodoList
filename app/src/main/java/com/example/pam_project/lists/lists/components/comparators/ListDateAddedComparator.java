package com.example.pam_project.lists.lists.components.comparators;

import com.example.pam_project.lists.lists.components.ListInformation;

public class ListDateAddedComparator extends ListInformationComparator {

    @Override
    public int compare(ListInformation o1, ListInformation o2) {
        int nullCmp = nullCompare(o1, o2);
        if (nullCmp != 0)
            return nullCmp;

        return Long.compare(o1.getId(), o2.getId());
    }
}
