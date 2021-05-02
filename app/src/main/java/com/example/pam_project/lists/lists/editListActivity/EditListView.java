package com.example.pam_project.lists.lists.editListActivity;

import com.example.pam_project.lists.categories.CategoryInformation;
import com.example.pam_project.lists.lists.components.ListInformation;

import java.util.List;

public interface EditListView {

    void bindCategories(final List<CategoryInformation> model);

    void bindList(final ListInformation model);

    void onSuccessfulUpdate(final String name, final long categoryId);

    void onFailedUpdate();
}
