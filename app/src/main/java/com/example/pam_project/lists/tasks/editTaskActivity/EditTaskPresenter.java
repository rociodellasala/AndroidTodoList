package com.example.pam_project.lists.tasks.editTaskActivity;

import com.example.pam_project.db.repositories.TaskRepository;
import com.example.pam_project.lists.tasks.components.TaskInformation;

import java.lang.ref.WeakReference;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class EditTaskPresenter {

    private final long taskId;
    private final TaskRepository repository;
    private final WeakReference<EditTaskView> view;

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

    public void editTask(final String name, final String description, final boolean priority) {
        Completable.fromAction(() -> {
            repository.updateTask(taskId, name, description, priority);
            if (view.get() != null) {
                view.get().onSuccessfulUpdate(name, description, priority);
            }
        }).onErrorComplete()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
}
