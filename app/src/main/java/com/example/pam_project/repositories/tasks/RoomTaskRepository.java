package com.example.pam_project.repositories.tasks;

import com.example.pam_project.database.tasks.TaskDao;
import com.example.pam_project.database.tasks.TaskEntity;
import com.example.pam_project.database.tasks.TaskMapper;
import com.example.pam_project.features.tasks.list.TaskInformation;
import com.example.pam_project.utils.constants.TaskStatus;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class RoomTaskRepository implements TaskRepository {

    private final TaskDao taskDao;
    private final TaskMapper mapper;

    public RoomTaskRepository(final TaskDao taskDao, final TaskMapper mapper) {
        this.taskDao = taskDao;
        this.mapper = mapper;
    }

    @Override
    public Flowable<TaskInformation> getTask(final long taskId) {
        return taskDao.getTaskById(taskId).map(mapper::toModel);
    }

    @Override
    public Completable insertTask(final String name, final String description, final boolean priority,
                                  final TaskStatus status, final long listId) {
        TaskEntity taskEntity = new TaskEntity(name, description, priority, TaskStatus.statusToString(status), listId);
        return Completable.fromAction(() -> taskDao.insertTask(taskEntity));
    }

    @Override
    public Completable updateTask(final long id, final String name, final String description, final boolean priority,
                                  final TaskStatus status, final long listId) {
        TaskEntity taskEntity = new TaskEntity(id, name, description, priority, TaskStatus.statusToString(status), listId);
        return Completable.fromAction(() -> taskDao.updateTask(taskEntity));
    }

    @Override
    public Completable updateTask(final long id, final String name, final String description, final boolean priority) {
        TaskEntity taskEntity = taskDao.getTaskById(id).blockingFirst();
        TaskEntity updatedTaskEntity = new TaskEntity(id, name, description, priority, taskEntity.status, taskEntity.listId);
        return Completable.fromAction(() -> taskDao.updateTask(updatedTaskEntity));
    }

    @Override
    public Completable deleteTask(long id) {
        TaskEntity taskEntity = taskDao.getTaskById(id).blockingFirst();
        return Completable.fromAction(() -> taskDao.deleteTask(taskEntity));
    }
}
