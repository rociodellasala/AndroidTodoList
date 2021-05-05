package com.example.pam_project.lists.categories.createCategoryActivity;

public interface CreateCategoryView {

    void onSuccessfulInsert(long id, String name, String color);

    void onFailedInsert();
}
