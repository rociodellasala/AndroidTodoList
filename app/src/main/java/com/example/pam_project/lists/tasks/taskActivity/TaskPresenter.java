package com.example.pam_project.lists.tasks.taskActivity;

import com.example.pam_project.db.repositories.ListsRepository;
import com.example.pam_project.db.repositories.TaskRepository;
import com.example.pam_project.lists.tasks.components.TaskInformation;

import java.lang.ref.WeakReference;
import java.util.Collections;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TaskPresenter {

    private final TaskRepository taskRepository;
    private final ListsRepository listsRepository;
    private final WeakReference<TaskView> view;
    private Disposable disposable;
    private long listId;

    public TaskPresenter(final TaskRepository taskRepository, final ListsRepository listsRepository,
                         final TaskView view, final long listId) {
        this.taskRepository = taskRepository;
        this.listsRepository = listsRepository;
        this.view = new WeakReference<>(view);
        this.listId = listId;
    }

    public void onViewAttached() {
        view.get().showTasks();
        fetchTasks();
    }

    private void fetchTasks(){
        disposable = listsRepository.getListWithTasks(listId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    Collections.sort(model.getTasks(), Collections.reverseOrder());
                    if(view.get() != null){
                        view.get().bindListName(model.getTitle());
                        view.get().bindTasks(model.getTasks());
                    }
                });
    }

    public void appendTask(final long id){
        TaskInformation model = taskRepository.getTask(id);
        if(view.get() != null)
            view.get().bindTask(model);
    }

    public void onViewDetached() {
        if(disposable != null)
            disposable.dispose();
    }

    public void onButtonAddClicked() {
        if(view.get() != null)
            view.get().showAddTask();
    }
}
