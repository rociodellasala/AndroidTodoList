package com.example.pam_project.lists.tasks.editTaskActivity;

import com.example.pam_project.lists.tasks.components.TaskInformation;

public interface EditTaskView {

    void bindTask(TaskInformation model);

    void onSuccessfulUpdate(String name, String description, boolean priority);

    void onFailedUpdate();
}
