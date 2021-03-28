package com.example.pam_project.lists.lists;

import com.example.pam_project.utils.AppColor;

public class ListInformation {
    private String title;
    private String numberOfTasks;
    private AppColor color;

    public ListInformation(final String title, final String numberOfTasks, AppColor color) {
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

    public AppColor getColor() {
        return color;
    }

}
