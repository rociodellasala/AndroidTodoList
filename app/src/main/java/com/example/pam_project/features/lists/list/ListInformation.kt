package com.example.pam_project.features.lists.list

import com.example.pam_project.comparators.ListAlphabeticalComparator
import com.example.pam_project.comparators.ListDateAddedComparator
import com.example.pam_project.comparators.ListInformationComparator
import com.example.pam_project.comparators.ListTaskNumberComparator
import com.example.pam_project.features.categories.list.CategoryInformation
import com.example.pam_project.features.tasks.list.TaskInformation
import com.example.pam_project.utils.constants.AppColor
import com.example.pam_project.utils.constants.TaskStatus
import java.io.Serializable
import java.util.*

class ListInformation(val id: Long, val title: String?, val categoryId: Long) : Serializable, Comparable<ListInformation> {
    var color: AppColor?
        private set
    var tasks: List<TaskInformation>?
        private set
    var category: CategoryInformation? = null

    constructor(id: Long, title: String?, categoryId: Long,
                color: AppColor?, tasks: List<TaskInformation>?) : this(id, title, categoryId) {
        this.color = color
        this.tasks = tasks
    }

    constructor(id: Long, title: String?, categoryId: Long,
                color: String?, tasks: List<TaskInformation>?) : this(id, title, categoryId, AppColor.Companion.fromName(color), tasks) {
    }

    constructor(id: Long, title: String?, categoryId: Long,
                color: String?) : this(id, title, categoryId, AppColor.Companion.fromName(color), null) {
    }

    constructor(id: Long, title: String?, categoryId: Long,
                tasks: List<TaskInformation>?) : this(id, title, categoryId, null as AppColor?, tasks) {
    }

    fun hasUrgentTask(): Boolean {
        for (task in tasks!!) {
            if (task.urgency && task.status == TaskStatus.PENDING) return true
        }
        return false
    }

    val pendingTaskCount: Int
        get() {
            if (tasks == null) return 0
            var completed = 0
            for (task in tasks!!) {
                if (task.status == TaskStatus.PENDING) completed++
            }
            return completed
        }
    val urgentPendingTaskCount: Int
        get() {
            if (tasks == null) return 0
            var urgent = 0
            for (task in tasks!!) {
                if (task.urgency && task.status == TaskStatus.PENDING) urgent++
            }
            return urgent
        }

    override fun compareTo(o: ListInformation): Int {
        return NATURAL_COMPARATOR.compare(this, o)
    }

    companion object {
        // based on R.array.sort_by_criteria
        private val NATURAL_COMPARATOR: ListInformationComparator = ListTaskNumberComparator()
        private val COMPARATORS = arrayOf(
                NATURAL_COMPARATOR,
                ListAlphabeticalComparator(),
                ListDateAddedComparator()
        )

        fun getComparator(index: Int): Comparator<ListInformation>? {
            return if (index >= COMPARATORS.size || index < 0) null else COMPARATORS[index]
        }
    }
}