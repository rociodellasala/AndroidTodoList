package com.example.pam_project.features.tasks.edit;

import com.example.pam_project.features.tasks.list.TaskInformation;

public interface EditTaskView {

    void bindTask(TaskInformation model);

    void onSuccessfulUpdate(String name, String description, boolean priority);

    void onFailedUpdate();
}
