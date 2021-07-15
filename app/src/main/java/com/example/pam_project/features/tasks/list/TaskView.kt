package com.example.pam_project.features.tasks.list

interface TaskView {
    fun bindTasks(model: List<TaskInformation?>?)
    fun bindHeaders(headers: IntArray)
    fun bindListName(name: String?)
    fun showAddTask()
    fun onTaskStatusEdit(model: TaskInformation?, adapterPosition: Int)
    fun showTaskContent(id: Long)
    fun showEmptyMessage()
    fun showEditList()
    fun onTasksReceivedError()
    fun onTaskUpdatedError()
}