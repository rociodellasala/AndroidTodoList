package com.example.pam_project.features.categories.create;

public interface CreateCategoryView {

    void onSuccessfulInsert(long id, String name, String color);

    void onFailedInsert();
}
