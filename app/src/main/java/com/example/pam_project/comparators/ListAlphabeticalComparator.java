package com.example.pam_project.comparators;

import com.example.pam_project.features.lists.list.ListInformation;

public class ListAlphabeticalComparator extends ListInformationComparator {

    @Override
    public int compare(ListInformation o1, ListInformation o2) {
        return String.CASE_INSENSITIVE_ORDER.compare(o1.getTitle(), o2.getTitle());
    }
}
