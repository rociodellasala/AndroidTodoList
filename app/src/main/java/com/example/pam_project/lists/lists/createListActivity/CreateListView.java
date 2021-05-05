package com.example.pam_project.lists.lists.createListActivity;

import com.example.pam_project.lists.categories.components.CategoryInformation;

import java.util.List;

public interface CreateListView {

    void bindCategories(final List<CategoryInformation> model);

    void onSuccessfulInsert();

    void onFailedInsert();

}
