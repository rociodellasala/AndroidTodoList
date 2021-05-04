package com.example.pam_project.lists.categories.createcategoryactivity;

public interface CreateCategoryView {

    void onSuccessfulInsert(long id, String name, String color);

    void onFailedInsert();
}
