package com.example.pam_project.db.mappers;

import com.example.pam_project.db.entities.TaskEntity;
import com.example.pam_project.lists.tasks.components.TaskInformation;
import com.example.pam_project.utils.StatusMapper;

public class TaskMapper {

    public TaskInformation toModel(final TaskEntity entity) {
        return new TaskInformation(entity.id, entity.name, entity.description,
                entity.priority, StatusMapper.toTaskStatusMapper(entity.status));
    }

}
