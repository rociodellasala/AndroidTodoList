package com.example.pam_project.lists.tasks.taskActivity;

import com.example.pam_project.lists.tasks.components.TaskInformation;

import java.util.List;

public interface TaskView {

    void showTasks();

    void bindTasks(final List<TaskInformation> model);

    void bindTask(final TaskInformation model);

    void showAddTask();

    void bindListName(final String name);
}