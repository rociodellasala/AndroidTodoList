package com.example.pam_project.features.tasks.edit;

import com.example.pam_project.features.tasks.list.TaskInformation;
import com.example.pam_project.repositories.tasks.TaskRepository;

import java.lang.ref.WeakReference;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EditTaskPresenter {

    private final long taskId;
    private final TaskRepository repository;
    private final WeakReference<EditTaskView> view;
    private final CompositeDisposable compositeDisposable;

    public EditTaskPresenter(final long taskId, final TaskRepository repository, final EditTaskView view) {
        this.taskId = taskId;
        this.repository = repository;
        this.view = new WeakReference<>(view);
        this.compositeDisposable = new CompositeDisposable();
    }

    public void onViewAttached() {
        if (view.get() != null) {
            TaskInformation model = repository.getTask(taskId);
            view.get().bindTask(model);
        }
    }

    public void editTask(final String name, final String description, final boolean priority) {
        Disposable disposable = Completable.fromAction(() -> {
            repository.updateTask(taskId, name, description, priority);
            if (view.get() != null) {
                view.get().onTaskEdit();
            }
        }).onErrorComplete()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();

        compositeDisposable.add(disposable);
    }

    public void deleteTask(long taskId) {
        Disposable disposable = Completable.fromAction(() -> {
            repository.deleteTask(taskId);
            if (view.get() != null) {
                view.get().onTaskDelete();
            }
        }).onErrorComplete()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();
        compositeDisposable.add(disposable);
    }

    public void onViewDetached() {
        compositeDisposable.dispose();
    }

}
