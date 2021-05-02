package com.example.pam_project.lists.categories;

import com.example.pam_project.utils.AppColor;

import java.io.Serializable;

public class CategoryInformation implements Serializable {
    private long id;
    private final String title;
    private final AppColor color;

    public CategoryInformation(final String title, AppColor color) {
        this.title = title;
        this.color = color;
    }

    public CategoryInformation(final long id, final String title, AppColor color) {
        this.title = title;
        this.color = color;
        this.id = id;
    }

    public long getId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public AppColor getColor() {
        return color;
    }
}
