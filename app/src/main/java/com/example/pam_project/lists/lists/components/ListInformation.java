package com.example.pam_project.lists.lists.components;

import com.example.pam_project.utils.AppColor;

import java.io.Serializable;

public class ListInformation implements Serializable {
    private long id;
    private final String title;
    private long categoryId;
    private AppColor color;


    public ListInformation(final long id, final String title, final long categoryId, AppColor color) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
        this.color = color;
    }

    public ListInformation(final long id ,final String title, final long categoryId){
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

}