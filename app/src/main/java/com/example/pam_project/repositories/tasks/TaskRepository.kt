package com.example.pam_project.repositories.tasks

import com.example.pam_project.features.tasks.list.TaskInformation
import com.example.pam_project.utils.constants.TaskStatus
import io.reactivex.Completable
import io.reactivex.Flowable

interface TaskRepository {
    fun getTask(id: Long): Flowable<TaskInformation?>
    fun insertTask(name: String?, description: String?, priority: Boolean, status: TaskStatus?, listId: Long): Completable
    fun updateTask(id: Long, name: String?, description: String?, priority: Boolean, status: TaskStatus?,
                   listId: Long): Completable

    fun updateTask(id: Long, name: String?, description: String?, priority: Boolean): Completable
    fun deleteTask(id: Long): Completable
}