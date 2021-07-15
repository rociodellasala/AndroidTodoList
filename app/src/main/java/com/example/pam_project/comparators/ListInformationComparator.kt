package com.example.pam_project.comparators

import com.example.pam_project.features.lists.list.ListInformation
import java.util.*

abstract class ListInformationComparator : Comparator<ListInformation?> {
    fun nullCompare(o1: Any?, o2: Any?): Int {
        if (o2 == null) return Int.MIN_VALUE
        return if (o1 == null) Int.MAX_VALUE else 0
    }
}