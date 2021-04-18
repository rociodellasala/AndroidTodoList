package com.example.pam_project.lists.categories;

import com.example.pam_project.utils.AppColor;

import java.io.Serializable;

public class CategoryInformation implements Serializable {
    private String title;
    private AppColor color;

    public CategoryInformation(final String title, AppColor color) {
        this.title = title;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public AppColor getColor() {
        return color;
    }
}
