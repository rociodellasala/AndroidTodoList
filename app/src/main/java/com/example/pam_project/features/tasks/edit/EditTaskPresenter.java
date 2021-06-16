package com.example.pam_project.features.tasks.edit;

import com.example.pam_project.features.tasks.list.TaskInformation;
import com.example.pam_project.repositories.tasks.TaskRepository;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.Disposable;

public class EditTaskPresenter {
    private final long taskId;
    private final SchedulerProvider provider;
    private final TaskRepository repository;
    private final WeakReference<EditTaskView> view;
    private Disposable fetchTaskDisposable;
    private Disposable updateTaskDisposable;
    private Disposable deleteTaskDisposable;

    public EditTaskPresenter(final long taskId, final SchedulerProvider provider,
                             final TaskRepository repository, final EditTaskView view) {
        this.taskId = taskId;
        this.provider = provider;
        this.repository = repository;
        this.view = new WeakReference<>(view);
    }

    public void onViewAttached() {
        fetchTaskDisposable = repository.getTask(taskId)
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .subscribe(this::onTaskRetrieved, this::onTaskRetrievedError);
    }

    private void onTaskRetrieved(final TaskInformation model) {
        if (view.get() != null) {
            view.get().bindTask(model);
        }
    }

    private void onTaskRetrievedError(final Throwable throwable) {
        if (view.get() != null) {
            view.get().onTaskRetrievedError();
        }
    }

    public void updateTask(final String name, final String description, final boolean priority) {
        updateTaskDisposable = repository.updateTask(taskId, name, description, priority)
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .doOnError(this::onTaskUpdatedError)
                .subscribe();
    }

    private void onTaskUpdatedError(final Throwable throwable) {
        if (view.get() != null) {
            view.get().onTaskUpdatedError();
        }
    }

    public void deleteTask(long taskId) {
        deleteTaskDisposable =  repository.deleteTask(taskId)
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .subscribe(this::onTaskDeleted, this::onTaskDeletedError);
    }

    private void onTaskDeleted() {
        if (view.get() != null) {
            view.get().onTaskDelete();
        }
    }

    private void onTaskDeletedError(final Throwable throwable) {
        if (view.get() != null) {
            view.get().onTaskDeletedError();
        }
    }

    public void onViewDetached() {
        if(updateTaskDisposable != null)
            updateTaskDisposable.dispose();
        if(deleteTaskDisposable != null)
            deleteTaskDisposable.dispose();
    }
}
