package com.example.pam_project.features.tasks.list

import com.example.pam_project.utils.constants.TaskStatus

class TaskInformation : Comparable<TaskInformation> {
    val title: String?
    val description: String?
    val urgency: Boolean
    private val status: TaskStatus?
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

    fun getStatus(): TaskStatus {
        return status!!
    }

    override fun equals(o: Any?): Boolean {
        if (o == null) return false
        if (o !is TaskInformation) return false
        val other = o
        return id == other.id && title == other.title && description == other.description && urgency == other.urgency && status == other.getStatus()
    }

    override fun compareTo(o: TaskInformation): Int {
        return getStatus().compareTo(o.getStatus())
    }
}