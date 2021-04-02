package com.example.pam_project.lists.tasks;

public class TaskInformation {
    private String title;
    private String description;

    public TaskInformation(final String title, final String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

}
