package com.example.pam_project.comparators

import com.example.pam_project.features.lists.list.ListInformation

class ListTaskNumberComparator : ListInformationComparator() {
    override fun compare(o1: ListInformation, o2: ListInformation): Int {
        if (o1 === o2) return 0
        val nullCmp = nullCompare(o1, o2)
        if (nullCmp != 0) return nullCmp
        val urgentTasks = Integer.compare(o1.urgentPendingTaskCount,
                o2.urgentPendingTaskCount) * 100
        return -1 * (urgentTasks +
                Integer.compare(o1.pendingTaskCount, o2.pendingTaskCount))
    }
}