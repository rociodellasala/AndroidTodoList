package com.example.pam_project.lists.tasks.taskActivity;

import com.example.pam_project.db.repositories.ListsRepository;
import com.example.pam_project.db.repositories.TaskRepository;
import com.example.pam_project.lists.tasks.components.TaskInformation;
import com.example.pam_project.utils.TaskStatus;

import java.lang.ref.WeakReference;
import java.util.Collections;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TaskPresenter {

    private final TaskRepository taskRepository;
    private final ListsRepository listsRepository;
    private final WeakReference<TaskView> view;
    private Disposable disposable;
    private final long listId;

    public TaskPresenter(final TaskRepository taskRepository, final ListsRepository listsRepository,
                         final TaskView view, final long listId) {
        this.taskRepository = taskRepository;
        this.listsRepository = listsRepository;
        this.view = new WeakReference<>(view);
        this.listId = listId;
    }

    public void onViewAttached() {
        if (view.get() != null) {
            view.get().showTasks();
            fetchTasks();
        }
    }

    private void fetchTasks() {
        disposable = listsRepository.getListWithTasks(listId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    Collections.sort(model.getTasks(), Collections.reverseOrder());
                    if (view.get() != null) {
                        view.get().bindListName(model.getTitle());
                        view.get().bindTasks(model.getTasks());
                    }
                });
    }

    public void onTaskChange(final int position, final long id, final String name,
                               final String description, final boolean priority,
                               final TaskStatus status, final long listId) {
        Completable.fromAction(() -> {
            taskRepository.updateTask(id, name, description, priority, status, listId);
            if (view.get() != null) {
                TaskInformation taskInformation = new TaskInformation(id, name, description, priority, status);
                view.get().onSuccessfulUpdate(taskInformation, position);
            }
        }).onErrorComplete()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void appendTask(final long id) {
        TaskInformation model = taskRepository.getTask(id);
        if (view.get() != null)
            view.get().bindTask(model);
    }

    public void onEmptyTask(){
        if (view.get() != null)
            view.get().showEmptyMessage();
    }

    public void onViewDetached() {
        if (disposable != null)
            disposable.dispose();
    }

    public void onButtonAddClicked() {
        if (view.get() != null)
            view.get().showAddTask();
    }

    public void onTaskClicked(final long id) {
        if (view.get() != null)
            view.get().showTaskContent(id);
    }
}
