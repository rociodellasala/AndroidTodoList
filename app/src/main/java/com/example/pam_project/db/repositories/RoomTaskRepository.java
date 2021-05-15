package com.example.pam_project.db.repositories;

import com.example.pam_project.db.daos.TaskDao;
import com.example.pam_project.db.entities.TaskEntity;
import com.example.pam_project.lists.tasks.components.TaskInformation;
import com.example.pam_project.utils.StatusMapper;
import com.example.pam_project.utils.TaskStatus;

public class RoomTaskRepository implements TaskRepository {

    private final TaskDao taskDao;

    public RoomTaskRepository(final TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public TaskInformation getTask(final long taskId) {
        TaskEntity taskEntity = taskDao.getTaskById(taskId).blockingFirst();
        return new TaskInformation(taskEntity.id, taskEntity.name, taskEntity.description,
                taskEntity.priority, StatusMapper.toTaskStatusMapper(taskEntity.status));
    }

    @Override
    public long insertTask(final String name, final String description, final boolean priority,
                           final TaskStatus status, final long listId) {
        TaskEntity taskEntity = new TaskEntity(name, description, priority, StatusMapper.toStringStatus(status), listId);
        return taskDao.insertTask(taskEntity);
    }

    @Override
    public void updateTask(final long id, final String name, final String description, final boolean priority,
                           final TaskStatus status, final long listId) {
        TaskEntity taskEntity = new TaskEntity(id, name, description, priority, StatusMapper.toStringStatus(status), listId);
        taskDao.updateTask(taskEntity);
    }

    @Override
    public void updateTask(final long id, final String name, final String description, final boolean priority) {
        TaskEntity taskEntity = taskDao.getTaskById(id).blockingFirst();
        TaskEntity updatedTaskEntity = new TaskEntity(id, name, description, priority, taskEntity.status, taskEntity.listId);
        taskDao.updateTask(updatedTaskEntity);
    }
}
