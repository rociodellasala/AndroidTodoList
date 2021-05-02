package com.example.pam_project.lists.lists.createListActivity;

import com.example.pam_project.lists.categories.CategoryInformation;

import java.util.List;

public interface CreateListView {

    void bindCategories(final List<CategoryInformation> model);

    void onSuccessfulInsert(final long id);

    void onFailedInsert();

}
