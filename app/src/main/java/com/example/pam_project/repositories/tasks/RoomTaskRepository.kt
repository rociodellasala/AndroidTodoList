package com.example.pam_project.repositories.tasks

import com.example.pam_project.database.tasks.TaskDao
import com.example.pam_project.database.tasks.TaskEntity
import com.example.pam_project.database.tasks.TaskMapper
import com.example.pam_project.features.tasks.list.TaskInformation
import com.example.pam_project.utils.constants.TaskStatus
import io.reactivex.Completable
import io.reactivex.Flowable

class RoomTaskRepository(private val taskDao: TaskDao?, private val mapper: TaskMapper?) : TaskRepository {
    override fun getTask(id: Long): Flowable<TaskInformation> {
        return taskDao!!.getTaskById(id).map { entity: TaskEntity? -> mapper!!.toModel(entity) }
    }

    override fun insertTask(name: String?, description: String?, priority: Boolean, status: TaskStatus?, listId: Long): Completable {
        val taskEntity = TaskEntity(name, description, priority, TaskStatus.statusToString(status), listId)
        return Completable.fromAction { taskDao!!.insertTask(taskEntity) }
    }

    override fun updateTask(id: Long, name: String?, description: String?, priority: Boolean,
                            status: TaskStatus?, listId: Long): Completable {
        val taskEntity = TaskEntity(id, name, description, priority, TaskStatus.statusToString(status), listId)
        return Completable.fromAction { taskDao!!.updateTask(taskEntity) }
    }

    override fun updateTask(id: Long, name: String?, description: String?, priority: Boolean): Completable {
        val taskEntity = taskDao!!.getTaskById(id).blockingFirst()
        val updatedTaskEntity = TaskEntity(id, name, description, priority, taskEntity!!.status, taskEntity.listId)
        return Completable.fromAction { taskDao.updateTask(updatedTaskEntity) }
    }

    override fun deleteTask(id: Long): Completable {
        val taskEntity = taskDao!!.getTaskById(id).blockingFirst()
        return Completable.fromAction { taskDao.deleteTask(taskEntity) }
    }
}