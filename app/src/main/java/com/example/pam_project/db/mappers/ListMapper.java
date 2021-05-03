package com.example.pam_project.db.mappers;

import com.example.pam_project.db.entities.ListEntity;
import com.example.pam_project.db.entities.TaskEntity;
import com.example.pam_project.db.relationships.ListsWithTasks;
import com.example.pam_project.lists.lists.components.ListInformation;
import com.example.pam_project.lists.tasks.components.TaskInformation;
import com.example.pam_project.utils.StatusMapper;

import java.util.ArrayList;
import java.util.List;

public class ListMapper {

    public ListInformation toModel(final ListEntity entity){
        return new ListInformation(entity.id, entity.name, entity.categoryId);
    }

    public ListInformation toModelWithTasks(final ListEntity entity, final List<TaskInformation> tasks){
        return new ListInformation(entity.id, entity.name, entity.categoryId, tasks);
    }

    public ListInformation toListWithTasksModel(final ListsWithTasks entity){
        final List<TaskInformation> listOfTasks = new ArrayList<>();

        for (final TaskEntity taskEntity : entity.tasks) {
            listOfTasks.add(new TaskInformation(taskEntity.id, taskEntity.name, taskEntity.description,
                    taskEntity.priority, StatusMapper.toTaskStatusMapper(taskEntity.status)));
        }

        return this.toModelWithTasks(entity.list, listOfTasks);
    }
}
