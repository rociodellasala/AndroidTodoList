package com.example.pam_project.features.tasks.list;

import com.example.pam_project.R;
import com.example.pam_project.repositories.lists.ListsRepository;
import com.example.pam_project.repositories.tasks.TaskRepository;
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
    private Disposable fetchTasksDisposable;
    private Disposable changeTaskDisposable;
    private final long listId;

    public TaskPresenter(final TaskRepository taskRepository, final ListsRepository listsRepository,
                         final TaskView view, final long listId) {
        this.taskRepository = taskRepository;
        this.listsRepository = listsRepository;
        this.view = new WeakReference<>(view);
        this.listId = listId;
        this.appendHeaders();
    }

    public void onViewAttached() {
        if (view.get() != null) {
            view.get().showTasks();
            fetchTasks();
        }
    }

    private void fetchTasks() {
        fetchTasksDisposable = listsRepository.getListWithTasks(listId)
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

    public void onTaskChange(TaskInformation taskInformation, int position){
        if (taskInformation.getStatus().equals(TaskStatus.PENDING)) {
            changeTask(position, taskInformation.getId(), taskInformation.getTitle(),
                    taskInformation.getDescription(), taskInformation.getUrgency(),
                    TaskStatus.DONE, listId);
        } else {
            changeTask(position, taskInformation.getId(), taskInformation.getTitle(),
                    taskInformation.getDescription(), taskInformation.getUrgency(),
                    TaskStatus.PENDING, listId);
        }
    }

    private void changeTask(final int position, final long id, final String name,
        final String description, final boolean priority,
        final TaskStatus status, final long listId) {
        changeTaskDisposable = Completable.fromAction(() -> {
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

    public void onEditList(){
        if (view.get() != null)
            view.get().showEditList();
    }

    public void onButtonAddClicked() {
        if (view.get() != null)
            view.get().showAddTask();
    }

    public void onTaskClicked(final long id) {
        if (view.get() != null)
            view.get().showTaskContent(id);
    }

    public void appendHeaders() {
        if (view.get() != null) {
            int[] headers = {R.string.pending_tasks, R.string.done_tasks};
            view.get().bindHeaders(headers);
        }
    }

    public void onViewDetached() {
        if (fetchTasksDisposable != null)
            fetchTasksDisposable.dispose();
        if (changeTaskDisposable != null)
            changeTaskDisposable.dispose();
    }
}
