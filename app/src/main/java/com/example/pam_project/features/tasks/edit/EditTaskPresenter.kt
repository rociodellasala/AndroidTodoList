package com.example.pam_project.features.tasks.edit

import com.example.pam_project.features.tasks.list.TaskInformation
import com.example.pam_project.repositories.tasks.TaskRepository
import com.example.pam_project.utils.schedulers.SchedulerProvider
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

class EditTaskPresenter(private val taskId: Long, private val provider: SchedulerProvider?,
                        private val repository: TaskRepository?, view: EditTaskView?) {
    private val view: WeakReference<EditTaskView?>
    private var fetchTaskDisposable: Disposable? = null
    private var updateTaskDisposable: Disposable? = null
    private var deleteTaskDisposable: Disposable? = null
    fun onViewAttached() {
        fetchTaskDisposable = repository!!.getTask(taskId)
                .subscribeOn(provider!!.computation())
                .observeOn(provider.ui())
                .subscribe({ model: TaskInformation? -> onTaskRetrieved(model) }) { throwable: Throwable -> onTaskRetrievedError(throwable) }
    }

    private fun onTaskRetrieved(model: TaskInformation?) {
        if (view.get() != null) {
            view.get()!!.bindTask(model)
        }
    }

    private fun onTaskRetrievedError(throwable: Throwable) {
        if (view.get() != null) {
            view.get()!!.onTaskRetrievedError()
        }
    }

    fun updateTask(name: String?, description: String?, priority: Boolean) {
        updateTaskDisposable = repository!!.updateTask(taskId, name, description, priority)
                .subscribeOn(provider!!.computation())
                .observeOn(provider.ui())
                .subscribe({}) { throwable: Throwable -> onTaskUpdatedError(throwable) }
    }

    private fun onTaskUpdatedError(throwable: Throwable) {
        if (view.get() != null) {
            view.get()!!.onTaskUpdatedError()
        }
    }

    fun deleteTask(taskId: Long) {
        deleteTaskDisposable = repository!!.deleteTask(taskId)
                .subscribeOn(provider!!.computation())
                .observeOn(provider.ui())
                .subscribe({ onTaskDeleted() }) { throwable: Throwable -> onTaskDeletedError(throwable) }
    }

    private fun onTaskDeleted() {
        if (view.get() != null) {
            view.get()!!.onTaskDelete()
        }
    }

    private fun onTaskDeletedError(throwable: Throwable) {
        if (view.get() != null) {
            view.get()!!.onTaskDeletedError()
        }
    }

    fun onDeletePressed() {
        if (view.get() != null) {
            view.get()!!.showDeleteDialog()
        }
    }

    fun onViewDetached() {
        if (updateTaskDisposable != null) updateTaskDisposable!!.dispose()
        if (deleteTaskDisposable != null) deleteTaskDisposable!!.dispose()
        if (fetchTaskDisposable != null) fetchTaskDisposable!!.dispose()
    }

    init {
        this.view = WeakReference(view)
    }
}