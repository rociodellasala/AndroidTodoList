package com.example.pam_project.repositories.tasks;

import com.example.pam_project.features.tasks.list.TaskInformation;
import com.example.pam_project.utils.constants.TaskStatus;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface TaskRepository {

    TaskInformation getTask(final long id);

    Completable insertTask(final String name, final String description, final boolean priority, final TaskStatus status, final long listId);

    Completable updateTask(final long id, final String name, final String description, final boolean priority, final TaskStatus status,
                           final long listId);

    Completable updateTask(final long id, final String name, final String description, final boolean priority);

    Completable deleteTask(final long id);
}
