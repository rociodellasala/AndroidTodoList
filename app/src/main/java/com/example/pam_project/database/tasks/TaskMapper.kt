package com.example.pam_project.database.tasks

import com.example.pam_project.features.tasks.list.TaskInformation
import com.example.pam_project.utils.constants.TaskStatus

class TaskMapper {
    fun toModel(entity: TaskEntity?): TaskInformation {
        return TaskInformation(entity!!.id, entity.name, entity.description,
                entity.priority, TaskStatus.Companion.getStatus(entity.status))
    }
}