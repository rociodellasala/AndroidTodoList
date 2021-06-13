package com.example.pam_project.features.lists.create;

import com.example.pam_project.features.categories.list.CategoryInformation;

import java.util.List;

public interface CreateListView {

    void bindCategories(final List<CategoryInformation> model);

    void onListInsertedError();

    void onCategoriesReceivedError();
}
