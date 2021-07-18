package com.example.pam_project.features.tasks.list

import com.example.pam_project.R
import com.example.pam_project.features.lists.list.ListInformation
import com.example.pam_project.repositories.lists.ListsRepository
import com.example.pam_project.repositories.tasks.TaskRepository
import com.example.pam_project.utils.constants.TaskStatus
import com.example.pam_project.utils.schedulers.SchedulerProvider
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference
import java.util.*

class TaskPresenter(private val provider: SchedulerProvider, private val taskRepository: TaskRepository,
                    private val listsRepository: ListsRepository, view: TaskView,
                    private val listId: Long) {
    private val view: WeakReference<TaskView> = WeakReference(view)
    private var fetchTasksDisposable: Disposable? = null
    private var updateTaskDisposable: Disposable? = null

    fun onViewAttached() {
        if (view.get() != null) {
            fetchTasks()
        }
    }

    private fun fetchTasks() {
        fetchTasksDisposable = listsRepository.getListWithTasks(listId)
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .subscribe({ model: ListInformation? -> onTasksReceived(model) }) { throwable: Throwable -> onTasksReceivedError(throwable) }
    }

    private fun onTasksReceived(model: ListInformation?) {
        Collections.sort(model?.tasks, Collections.reverseOrder())
        if (view.get() != null) {
            view.get()!!.bindListName(model?.title)
            view.get()!!.bindTasks(model?.tasks)
        }
    }

    private fun onTasksReceivedError(throwable: Throwable) {
        if (view.get() != null) {
            view.get()!!.onTasksReceivedError()
        }
    }

    fun onTaskChange(taskInformation: TaskInformation?, position: Int) {
        if (taskInformation!!.status == TaskStatus.PENDING) {
            updateTask(position, taskInformation.id, taskInformation.title,
                    taskInformation.description, taskInformation.urgency,
                    TaskStatus.DONE, listId)
        } else {
            updateTask(position, taskInformation.id, taskInformation.title,
                    taskInformation.description, taskInformation.urgency,
                    TaskStatus.PENDING, listId)
        }
    }

    private fun updateTask(position: Int, id: Long, name: String?,
                           description: String?, priority: Boolean,
                           status: TaskStatus, listId: Long) {
        updateTaskDisposable = taskRepository.updateTask(id, name, description, priority, status, listId)
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .subscribe({
                    if (view.get() != null) {
                        val taskInformation = TaskInformation(id, name, description, priority, status)
                        view.get()!!.onTaskStatusEdit(taskInformation, position)
                    }
                }) { throwable: Throwable -> onTaskUpdatedError(throwable) }
    }

    private fun onTaskUpdatedError(throwable: Throwable) {
        if (view.get() != null) {
            view.get()!!.onTaskUpdatedError()
        }
    }

    fun onEmptyTask() {
        if (view.get() != null) view.get()!!.showEmptyMessage()
    }

    fun onEditList() {
        if (view.get() != null) view.get()!!.showEditList()
    }

    fun onButtonAddClicked() {
        if (view.get() != null) view.get()!!.showAddTask()
    }

    fun onTaskClicked(id: Long) {
        if (view.get() != null) view.get()!!.showTaskContent(id)
    }

    private fun appendHeaders() {
        if (view.get() != null) {
            val headers = intArrayOf(R.string.pending_tasks, R.string.done_tasks)
            view.get()!!.bindHeaders(headers)
        }
    }

    fun onViewDetached() {
        if (fetchTasksDisposable != null) fetchTasksDisposable!!.dispose()
        if (updateTaskDisposable != null) updateTaskDisposable!!.dispose()
    }

    init {
        appendHeaders()
    }
}