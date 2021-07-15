package com.example.pam_project.features.tasks.edit

import com.example.pam_project.features.tasks.list.TaskInformation

interface EditTaskView {
    fun bindTask(model: TaskInformation?)
    fun onTaskDelete()
    fun onTaskUpdatedError()
    fun onTaskDeletedError()
    fun showDeleteDialog()
    fun onTaskRetrievedError()
}