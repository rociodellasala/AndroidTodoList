package com.example.pam_project.lists.categories.components;

import com.example.pam_project.utils.AppColor;

import java.io.Serializable;

public class CategoryInformation implements Serializable, Comparable<CategoryInformation> {
    private final String title;
    private final AppColor color;
    private long id;

    public CategoryInformation(final String title, final AppColor color) {
        this.title = title;
        this.color = color;
    }

    public CategoryInformation(final String title, final String color) {
        this(title, AppColor.fromName(color));
    }

    public CategoryInformation(final long id, final String title, final AppColor color) {
        this(title, color);
        this.id = id;
    }

    public CategoryInformation(final long id, final String title, final String color) {
        this(id, title, AppColor.fromName(color));
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
