package com.example.pam_project.features.tasks.create;

import com.example.pam_project.repositories.tasks.TaskRepository;
import com.example.pam_project.utils.constants.TaskStatus;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CreateTaskPresenter {
    private final SchedulerProvider provider;
    private final TaskRepository taskRepository;
    private final WeakReference<CreateTaskView> view;
    private Disposable insertTaskDisposable;

    public CreateTaskPresenter(final SchedulerProvider provider,
                               final TaskRepository taskRepository, final CreateTaskView view) {
        this.provider = provider;
        this.taskRepository = taskRepository;
        this.view = new WeakReference<>(view);
    }

    public void insertTask(final String name, final String description, final boolean priority, final long listId) {
        insertTaskDisposable = taskRepository.insertTask(name, description, priority, TaskStatus.PENDING, listId)
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .subscribe(this::onTaskInserted, this::onTaskInsertedError);
    }

    private void onTaskInserted() {
       // TODO
    }

    private void onTaskInsertedError(final Throwable throwable) {
        // TODO
    }

    public void onViewDetached() {
        if (insertTaskDisposable != null)
            insertTaskDisposable.dispose();
    }
}
