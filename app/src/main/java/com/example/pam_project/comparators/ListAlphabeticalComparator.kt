package com.example.pam_project.comparators

import com.example.pam_project.features.lists.list.ListInformation
import java.lang.String

class ListAlphabeticalComparator : ListInformationComparator() {
    override fun compare(o1: ListInformation?, o2: ListInformation?): Int {
        return String.CASE_INSENSITIVE_ORDER.compare(o1?.title, o2?.title)
    }
}