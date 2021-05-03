package com.example.pam_project.lists.tasks.createTaskActivity;

import com.example.pam_project.db.repositories.TaskRepository;
import com.example.pam_project.utils.TaskStatus;

import java.lang.ref.WeakReference;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CreateTaskPresenter {
    private final TaskRepository taskRepository;
    private final WeakReference<CreateTaskView> view;

    public CreateTaskPresenter(final TaskRepository taskRepository, final CreateTaskView view) {
        this.taskRepository = taskRepository;
        this.view = new WeakReference<>(view);
    }

    public void insertTask(final String name, final String description, final boolean priority, final long listId){
        Completable.fromAction(() -> {
            long id = taskRepository.insertTask(name, description, priority, TaskStatus.PENDING, listId);
            if(view.get() != null){
                view.get().onSuccessfulInsert(id);
            }
        }).onErrorComplete()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

}
