package com.example.pam_project.features.tasks.edit;

import com.example.pam_project.features.tasks.list.TaskInformation;
import com.example.pam_project.repositories.tasks.TaskRepository;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EditTaskPresenter {
    private final long taskId;
    private final TaskRepository repository;
    private final WeakReference<EditTaskView> view;
    private Disposable updateTaskDisposable;
    private Disposable deleteTaskDisposable;

    public EditTaskPresenter(final long taskId, final TaskRepository repository, final EditTaskView view) {
        this.taskId = taskId;
        this.repository = repository;
        this.view = new WeakReference<>(view);
    }

    public void onViewAttached() {
        if (view.get() != null) {
            TaskInformation model = repository.getTask(taskId);
            view.get().bindTask(model);
        }
    }

    public void updateTask(final String name, final String description, final boolean priority) {
        updateTaskDisposable = repository.updateTask(taskId, name, description, priority)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onTaskUpdated, this::onTaskUpdatedError);
    }

    private void onTaskUpdated() {
        if (view.get() != null) {
            view.get().onTaskEdit();
        }
    }

    private void onTaskUpdatedError(final Throwable throwable) {
        // TODO
    }

    public void deleteTask(long taskId) {
        deleteTaskDisposable =  repository.deleteTask(taskId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onTaskDeleted, this::onTaskDeletedError);
    }

    private void onTaskDeleted() {
        if (view.get() != null) {
            view.get().onTaskDelete();
        }
    }

    private void onTaskDeletedError(final Throwable throwable) {
        // TODO
    }

    public void onViewDetached() {
        if(updateTaskDisposable != null)
            updateTaskDisposable.dispose();
        if(deleteTaskDisposable != null)
            deleteTaskDisposable.dispose();
    }
}
