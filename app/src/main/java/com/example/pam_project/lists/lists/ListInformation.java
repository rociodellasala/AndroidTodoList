package com.example.pam_project.lists.lists;

import com.example.pam_project.utils.AppColor;

import java.io.Serializable;

public class ListInformation implements Serializable {
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
