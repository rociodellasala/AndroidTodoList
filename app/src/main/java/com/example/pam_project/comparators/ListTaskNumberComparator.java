package com.example.pam_project.comparators;

import com.example.pam_project.features.lists.list.ListInformation;

public class ListTaskNumberComparator extends ListInformationComparator {

    @Override
    public int compare(ListInformation o1, ListInformation o2) {
        if (o1 == o2)
            return 0;

        int nullCmp = nullCompare(o1, o2);
        if (nullCmp != 0)
            return nullCmp;

        int urgentTasks = Integer.compare(o1.getUrgentPendingTaskCount(),
                o2.getUrgentPendingTaskCount()) * 100;
        return -1 * (urgentTasks +
                Integer.compare(o1.getPendingTaskCount(), o2.getPendingTaskCount()));
    }
}
