package com.example.pam_project.lists.lists.components;

import com.example.pam_project.lists.tasks.components.TaskInformation;
import com.example.pam_project.utils.AppColor;

import java.io.Serializable;
import java.util.List;

public class ListInformation implements Serializable, Comparable<ListInformation> {
    private final String title;
    private long id;
    private long categoryId;
    private AppColor color;
    private List<TaskInformation> tasks;

    public ListInformation(final long id, final String title, final long categoryId, AppColor color) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
        this.color = color;
    }

    public ListInformation(final long id, final String title, final long categoryId, AppColor color, List<TaskInformation> tasks) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
        this.color = color;
        this.tasks = tasks;
    }


    public ListInformation(final long id, final String title, final long categoryId, List<TaskInformation> tasks) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
        this.tasks = tasks;
    }

    public ListInformation(final long id, final String title, final long categoryId) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
    }

    public ListInformation(final String title, AppColor color) {
        this.title = title;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public AppColor getColor() {
        return color;
    }

    public List<TaskInformation> getTasks() {
        return tasks;
    }

    @Override
    public int compareTo(ListInformation o) {
        return Long.compare(this.id, o.getId());
    }
}