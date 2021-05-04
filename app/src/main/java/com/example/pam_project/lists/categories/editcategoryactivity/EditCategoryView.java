package com.example.pam_project.lists.categories.editcategoryactivity;

import com.example.pam_project.lists.categories.components.CategoryInformation;

public interface EditCategoryView {

    void bindCategory(CategoryInformation model);

    void onSuccessfulUpdate(String name, String color);

    void onFailedUpdate();
}
