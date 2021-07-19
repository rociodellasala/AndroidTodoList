package com.example.pam_project.features.tasks.list

import com.example.pam_project.utils.constants.TaskStatus

// TODO: Lo mismo, ver que onda porque para mi deberia ser privado
open class TaskInformation : Comparable<TaskInformation> {
    val title: String?
    val description: String?
    val urgency: Boolean
    val status: TaskStatus?
    var id: Long = 0
        private set

    constructor(id: Long, title: String?, description: String?, isUrgent: Boolean, status: TaskStatus?) {
        this.id = id
        this.title = title
        this.description = description
        urgency = isUrgent
        this.status = status
    }

    constructor(title: String?, description: String?, isUrgent: Boolean, status: TaskStatus?) {
        this.title = title
        this.description = description
        urgency = isUrgent
        this.status = status
    }

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