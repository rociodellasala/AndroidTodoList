package com.example.pam_project.features.tasks.list;

import com.example.pam_project.R;
import com.example.pam_project.features.lists.list.ListInformation;
import com.example.pam_project.repositories.lists.ListsRepository;
import com.example.pam_project.repositories.tasks.TaskRepository;
import com.example.pam_project.utils.constants.TaskStatus;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

import java.lang.ref.WeakReference;
import java.util.Collections;

import io.reactivex.disposables.Disposable;

public class TaskPresenter {
    private final SchedulerProvider provider;
    private final TaskRepository taskRepository;
    private final ListsRepository listsRepository;
    private final WeakReference<TaskView> view;
    private Disposable fetchTasksDisposable;
    private Disposable updateTaskDisposable;
    private final long listId;

    public TaskPresenter(final SchedulerProvider provider, final TaskRepository taskRepository,
                         final ListsRepository listsRepository, final TaskView view,
                         final long listId) {
        this.provider = provider;
        this.taskRepository = taskRepository;
        this.listsRepository = listsRepository;
        this.view = new WeakReference<>(view);
        this.listId = listId;
        this.appendHeaders();
    }

    public void onViewAttached() {
        if (view.get() != null) {
            fetchTasks();
        }
    }

    private void fetchTasks() {
        fetchTasksDisposable = listsRepository.getListWithTasks(listId)
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .subscribe(this::onTasksReceived, this::onTasksReceivedError);
    }

    private void onTasksReceived(final ListInformation model) {
        Collections.sort(model.getTasks(), Collections.reverseOrder());
        if (view.get() != null) {
            view.get().bindListName(model.getTitle());
            view.get().bindTasks(model.getTasks());
        }
    }

    private void onTasksReceivedError(final Throwable throwable) {
        // TODO
    }

    public void onTaskChange(TaskInformation taskInformation, int position){
        if (taskInformation.getStatus().equals(TaskStatus.PENDING)) {
            updateTask(position, taskInformation.getId(), taskInformation.getTitle(),
                    taskInformation.getDescription(), taskInformation.getUrgency(),
                    TaskStatus.DONE, listId);
        } else {
            updateTask(position, taskInformation.getId(), taskInformation.getTitle(),
                    taskInformation.getDescription(), taskInformation.getUrgency(),
                    TaskStatus.PENDING, listId);
        }
    }

    private void updateTask(final int position, final long id, final String name,
        final String description, final boolean priority,
        final TaskStatus status, final long listId) {
        updateTaskDisposable = taskRepository.updateTask(id, name, description, priority, status, listId)
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .subscribe(() -> {
                    if (view.get() != null) {
                        TaskInformation taskInformation = new TaskInformation(id, name, description, priority, status);
                        view.get().onTaskStatusEdit(taskInformation, position);
                    }
                }, this::onTaskUpdatedError);
    }

    private void onTaskUpdatedError(final Throwable throwable) {
        // TODO
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
        if (updateTaskDisposable != null)
            updateTaskDisposable.dispose();
    }
}
