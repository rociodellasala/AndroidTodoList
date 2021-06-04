package com.example.pam_project.comparators;

import com.example.pam_project.features.lists.list.ListInformation;

public class ListAlphabeticalComparator extends ListInformationComparator {

    @Override
    public int compare(ListInformation o1, ListInformation o2) {
        if (o1 == o2)
            return 0;

        int nullCmp = nullCompare(o1, o2);
        if (nullCmp != 0)
            return nullCmp;

        return o1.getTitle().compareTo(o2.getTitle());
    }
}
