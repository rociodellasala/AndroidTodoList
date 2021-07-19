package com.example.pam_project.features.tasks.list

import com.example.pam_project.utils.constants.TaskStatus

class TaskInformation(id: Long, title: String?, description: String?, isUrgent: Boolean,
                      status: TaskStatus?) : Comparable<TaskInformation> {

    var title: String? = title
        private set
    var description: String? = description
        private set
    var urgency: Boolean = isUrgent
        private set
    var status: TaskStatus? = status
        private set
    var id: Long = id
        private set

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is TaskInformation) return false
        return id == other.id && title == other.title && description == other.description
                && urgency == other.urgency && status == other.status
    }

    override fun compareTo(other: TaskInformation): Int {
        return status!!.compareTo(other.status!!)
    }

    override fun hashCode(): Int {
        var result = title?.hashCode() ?: 0
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + urgency.hashCode()
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + id.hashCode()
        return result
    }
}