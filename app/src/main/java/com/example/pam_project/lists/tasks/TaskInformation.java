package com.example.pam_project.lists.tasks;

import com.example.pam_project.utils.TaskStatus;

public class TaskInformation implements Comparable<TaskInformation> {
    private long id;
    private final String title;
    private final String description;
    private final boolean isUrgent;
    private final TaskStatus status;

    public TaskInformation(final long id, final String title, final String description, final boolean isUrgent, final TaskStatus status) {
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

    public long getId() { return id; }

    public String getDescription() {
        return description;
    }

    public boolean getUrgency() { return isUrgent; }

    public TaskStatus getStatus() { return status; }

    @Override
    public int compareTo(TaskInformation o) {
        return this.getStatus().compareTo(o.getStatus());
    }
}