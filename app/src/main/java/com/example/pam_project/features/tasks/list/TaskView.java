package com.example.pam_project.features.tasks.list;

import java.util.List;

public interface TaskView {

    void showTasks();

    void bindTasks(final List<TaskInformation> model);

    void bindTask(TaskInformation model);

    void bindHeaders(int[] headers);

    void bindListName(final String name);

    void showAddTask();

    void onTaskStatusEdit(final TaskInformation model, final int adapterPosition);

    void showTaskContent(final long id);

    void showEmptyMessage();

    void showEditList();
}