package com.example.pam_project.lists.tasks;

import com.example.pam_project.utils.TaskStatus;

public class TaskInformation {
    private int id;
    private String title;
    private String description;
    private boolean isUrgent;
    private TaskStatus status;

    public TaskInformation(final int id, final String title, final String description, final boolean isUrgent, final TaskStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isUrgent = isUrgent;
        this.status = status;
    }

    public TaskInformation(final String title, final String description, final boolean isUrgent, final TaskStatus status) {
        this.title = title;
        this.description = description;
        this.isUrgent = isUrgent;
        this.status = status;
    }


    public String getTitle() {
        return title;
    }

    public int getId() { return id; }

    public String getDescription() {
        return description;
    }

    public boolean getUrgency() { return isUrgent; }

    public TaskStatus getStatus() { return status; }

}
