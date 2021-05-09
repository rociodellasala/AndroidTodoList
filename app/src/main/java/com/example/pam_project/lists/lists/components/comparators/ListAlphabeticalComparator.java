package com.example.pam_project.lists.lists.components.comparators;

import com.example.pam_project.lists.lists.components.ListInformation;

public class ListAlphabeticalComparator extends ListInformationComparator {

    @Override
    public int compare(ListInformation o1, ListInformation o2) {
        int nullCmp = nullCompare(o1, o2);
        if (nullCmp != 0)
            return nullCmp;

        return o1.getTitle().compareTo(o2.getTitle());
    }
}
