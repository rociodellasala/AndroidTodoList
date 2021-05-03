package com.example.pam_project.lists.tasks.createTaskActivity;

public interface CreateTaskView {
    void onSuccessfulInsert(final long id);

    void onFailedInsert();
}