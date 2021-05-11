package com.example.pam_project.db.repositories;

import com.example.pam_project.lists.tasks.components.TaskInformation;
import com.example.pam_project.utils.TaskStatus;

public interface TaskRepository {

    TaskInformation getTask(final long id);

    long insertTask(final String name, final String description, final boolean priority, final TaskStatus status, final long listId);

    void updateTask(final long id, final String name, final String description,
                    final boolean priority, final TaskStatus status, final long listId);

    void updateTask(final long id, final String name, final String description,
                    final boolean priority);
}
