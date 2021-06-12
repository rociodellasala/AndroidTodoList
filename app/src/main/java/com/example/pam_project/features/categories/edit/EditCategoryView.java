package com.example.pam_project.features.categories.edit;

import com.example.pam_project.features.categories.list.CategoryInformation;

public interface EditCategoryView {

    void bindCategory(CategoryInformation model);

    void onCategoryEdit();

    void onCategoryDelete();
}
