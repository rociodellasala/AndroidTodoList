package com.example.pam_project.lists.lists;

import com.example.pam_project.utils.AppColor;

import java.io.Serializable;

public class ListInformation implements Serializable {
    private int id;
    private String title;
//    private int numberOfTasks;
    private AppColor color;

    public ListInformation(final int id, final String title, /* final int numberOfTasks,*/ AppColor color) {
        this.id = id;
        this.title = title;
//        this.numberOfTasks = numberOfTasks;
        this.color = color;
    }


    public ListInformation(final String title, /* final int numberOfTasks,*/ AppColor color) {
        this.title = title;
//        this.numberOfTasks = numberOfTasks;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

//    public int getNumberOfTasks() {
//        return numberOfTasks;
//    }

    public AppColor getColor() {
        return color;
    }

}
