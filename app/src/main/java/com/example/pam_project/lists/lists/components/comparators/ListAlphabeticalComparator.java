package com.example.pam_project.lists.lists.components.comparators;

import com.example.pam_project.lists.lists.components.ListInformation;

public class ListAlphabeticalComparator extends ListInformationComparator {

    @Override
    public int compare(ListInformation o1, ListInformation o2) {
        return String.CASE_INSENSITIVE_ORDER.compare(o1.getTitle(), o2.getTitle());
    }
}
