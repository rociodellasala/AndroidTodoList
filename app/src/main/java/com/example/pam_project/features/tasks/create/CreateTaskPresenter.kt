package com.example.pam_project.features.tasks.create

import com.example.pam_project.repositories.tasks.TaskRepository
import com.example.pam_project.utils.constants.TaskStatus
import com.example.pam_project.utils.schedulers.SchedulerProvider
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

class CreateTaskPresenter(private val provider: SchedulerProvider?, private val taskRepository: TaskRepository?, view: CreateTaskView?) {
    private val view: WeakReference<CreateTaskView?> = WeakReference(view)
    private var insertTaskDisposable: Disposable? = null

    fun insertTask(name: String?, description: String?, priority: Boolean, listId: Long) {
        insertTaskDisposable = taskRepository!!.insertTask(name, description, priority, TaskStatus.PENDING, listId)
                .subscribeOn(provider!!.computation())
                .observeOn(provider.ui())
                .subscribe({}) { throwable: Throwable -> onTaskInsertedError(throwable) }
    }

    private fun onTaskInsertedError(throwable: Throwable) {
        if (view.get() != null) {
            view.get()!!.onTaskInsertedError()
        }
    }

    fun onViewDetached() {
        if (insertTaskDisposable != null) insertTaskDisposable!!.dispose()
    }

}