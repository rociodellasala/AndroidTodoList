package com.example.pam_project.db.repositories;

import com.example.pam_project.db.daos.TaskDao;
import com.example.pam_project.db.entities.TaskEntity;
import com.example.pam_project.db.mappers.TaskMapper;
import com.example.pam_project.lists.tasks.components.TaskInformation;
import com.example.pam_project.utils.StatusMapper;
import com.example.pam_project.utils.TaskStatus;

public class RoomTaskRepository implements TaskRepository {

    private final TaskDao taskDao;
    private final TaskMapper mapper;

    public RoomTaskRepository(final TaskDao taskDao, final TaskMapper mapper) {
        this.taskDao = taskDao;
        this.mapper = mapper;
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
}
