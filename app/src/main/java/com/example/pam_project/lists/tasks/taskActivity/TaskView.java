package com.example.pam_project.lists.tasks.taskActivity;

import com.example.pam_project.lists.tasks.components.TaskInformation;

import java.util.List;

public interface TaskView {

    void showTasks();

    void bindTasks(final List<TaskInformation> model);

    void bindTask(TaskInformation model);

    void bindListName(final String name);

    void showAddTask();

    void onSuccessfulUpdate(final TaskInformation model, final int adapterPosition);

    void showTaskContent(final long id);
}