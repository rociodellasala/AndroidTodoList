package com.example.pam_project.database.lists;

import com.example.pam_project.database.relationships.ListsWithTasks;
import com.example.pam_project.database.tasks.TaskEntity;
import com.example.pam_project.features.lists.list.ListInformation;
import com.example.pam_project.features.tasks.list.TaskInformation;
import com.example.pam_project.utils.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public class ListMapper {

    public ListInformation toModel(final ListEntity entity) {
        return new ListInformation(entity.id, entity.name, entity.categoryId);
    }

    public ListInformation toModelWithTasks(final ListEntity entity, final List<TaskInformation> tasks) {
        return new ListInformation(entity.id, entity.name, entity.categoryId, tasks);
    }

    public ListInformation toListWithTasksModel(final ListsWithTasks entity) {
        final List<TaskInformation> listOfTasks = new ArrayList<>();

        for (final TaskEntity taskEntity : entity.tasks) {
            listOfTasks.add(new TaskInformation(taskEntity.id, taskEntity.name, taskEntity.description,
                    taskEntity.priority, TaskStatus.getStatus(taskEntity.status)));
        }

        return this.toModelWithTasks(entity.list, listOfTasks);
    }
}
