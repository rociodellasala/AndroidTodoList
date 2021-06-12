package com.example.pam_project.features.tasks.create;

import com.example.pam_project.repositories.tasks.TaskRepository;
import com.example.pam_project.utils.TaskStatus;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CreateTaskPresenter {
    private final TaskRepository taskRepository;
    private final WeakReference<CreateTaskView> view;
    private Disposable insertTaskDisposable;

    public CreateTaskPresenter(final TaskRepository taskRepository, final CreateTaskView view) {
        this.taskRepository = taskRepository;
        this.view = new WeakReference<>(view);
    }

    public void insertTask(final String name, final String description, final boolean priority, final long listId) {
        insertTaskDisposable = taskRepository.insertTask(name, description, priority, TaskStatus.PENDING, listId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onTaskInserted, this::onTaskInsertedError);
    }

    private void onTaskInserted() {
        if (view.get() != null) {
            view.get().onTaskCreate();
        }
    }

    private void onTaskInsertedError(final Throwable throwable) {
        // TODO
    }

    public void onViewDetached() {
        if (insertTaskDisposable != null)
            insertTaskDisposable.dispose();
    }
}
