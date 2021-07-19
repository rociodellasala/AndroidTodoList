package com.example.pam_project.database.tasks

import com.example.pam_project.features.tasks.list.TaskInformation
import com.example.pam_project.utils.constants.TaskStatus

class TaskMapper {
    fun toModel(entity: TaskEntity?): TaskInformation {
        return TaskInformation(
            id = entity!!.id,
            title = entity.name,
            description = entity.description,
            isUrgent = entity.priority,
            status = TaskStatus.getStatus(entity.status)
        )
    }
}