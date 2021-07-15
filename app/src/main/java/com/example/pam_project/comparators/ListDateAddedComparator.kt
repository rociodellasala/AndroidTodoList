package com.example.pam_project.comparators

import com.example.pam_project.features.lists.list.ListInformation

class ListDateAddedComparator : ListInformationComparator() {
    override fun compare(o1: ListInformation, o2: ListInformation): Int {
        if (o1 === o2) return 0
        val nullCmp = nullCompare(o1, o2)
        return if (nullCmp != 0) nullCmp else java.lang.Long.compare(o1.id, o2.id)
    }
}