package com.example.pam_project.lists.categories.components;

import com.example.pam_project.utils.AppColor;

import java.io.Serializable;

public class CategoryInformation implements Serializable, Comparable<CategoryInformation> {
    private final String title;
    private final AppColor color;
    private long id;

    public CategoryInformation(final String title, AppColor color) {
        this.title = title;
        this.color = color;
    }

    public CategoryInformation(final long id, final String title, AppColor color) {
        this.title = title;
        this.color = color;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public AppColor getColor() {
        return color;
    }

    @Override
    public int compareTo(CategoryInformation categoryInformation) {
        return title.compareTo(categoryInformation.getTitle());
    }
}
