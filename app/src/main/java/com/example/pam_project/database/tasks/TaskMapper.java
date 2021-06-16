package com.example.pam_project.database.tasks;

import com.example.pam_project.features.tasks.list.TaskInformation;
import com.example.pam_project.utils.constants.TaskStatus;

public class TaskMapper {

    public TaskInformation toModel(final TaskEntity entity) {
        return new TaskInformation(entity.id, entity.name, entity.description,
                entity.priority, TaskStatus.getStatus(entity.status));
    }
}
