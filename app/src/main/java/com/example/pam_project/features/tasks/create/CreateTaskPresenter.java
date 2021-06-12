package com.example.pam_project.features.tasks.create;

import com.example.pam_project.repositories.tasks.TaskRepository;
import com.example.pam_project.utils.TaskStatus;

import java.lang.ref.WeakReference;

import io.reactivex.Completable;
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
        insertTaskDisposable = Completable.fromAction(() -> {
            taskRepository.insertTask(name, description, priority, TaskStatus.PENDING, listId);
            if (view.get() != null) {
                view.get().onTaskCreate();
            }
        }).onErrorComplete()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void onViewDetached() {
        if (insertTaskDisposable != null)
            insertTaskDisposable.dispose();
    }

}
