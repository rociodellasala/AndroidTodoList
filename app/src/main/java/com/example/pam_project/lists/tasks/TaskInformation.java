package com.example.pam_project.lists.tasks;

import android.util.Log;

public class TaskInformation {
    private String title;
    private String description;
    private boolean isUrgent;

    public TaskInformation(final String title, final String description, final boolean isUrgent) {
        this.title = title;
        this.description = description;
        this.isUrgent = isUrgent;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean getUrgency() { return isUrgent; }

}
