package com.example.pam_project.features.tasks.list;

import com.example.pam_project.utils.constants.TaskStatus;

public class TaskInformation implements Comparable<TaskInformation> {
    private final String title;
    private final String description;
    private final boolean isUrgent;
    private final TaskStatus status;
    private long id;

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

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean getUrgency() {
        return isUrgent;
    }

    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof TaskInformation))
            return false;
        TaskInformation other = (TaskInformation) o;
        return id == other.getId() && title.equals(other.getTitle()) &&
                description.equals(other.getDescription()) && isUrgent == other.getUrgency() &&
                status.equals(other.getStatus());
    }

    @Override
    public int compareTo(TaskInformation o) {
        return this.getStatus().compareTo(o.getStatus());
    }
}