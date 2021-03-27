package com.example.pam_project.lists.lists;

public class ListInformation {
    private String title;
    private String numberOfTasks;
    private String color;

    public ListInformation(final String title, final String numberOfTasks, String color) {
        this.title = title;
        this.numberOfTasks = numberOfTasks;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public String getNumberOfTasks() {
        return numberOfTasks;
    }

    public String getColor() {
        return color;
    }

}
