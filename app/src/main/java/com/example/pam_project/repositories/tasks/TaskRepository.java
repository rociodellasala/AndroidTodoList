package com.example.pam_project.repositories.tasks;

import com.example.pam_project.features.tasks.list.TaskInformation;
import com.example.pam_project.utils.TaskStatus;

public interface TaskRepository {

    TaskInformation getTask(final long id);

    long insertTask(final String name, final String description, final boolean priority, final TaskStatus status, final long listId);

    void updateTask(final long id, final String name, final String description,
                    final boolean priority, final TaskStatus status, final long listId);

    void updateTask(final long id, final String name, final String description,
                    final boolean priority);
}
