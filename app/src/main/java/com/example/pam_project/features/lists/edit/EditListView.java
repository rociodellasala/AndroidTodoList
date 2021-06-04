package com.example.pam_project.features.lists.edit;

import com.example.pam_project.features.categories.list.CategoryInformation;
import com.example.pam_project.features.lists.list.ListInformation;

import java.util.List;

public interface EditListView {

    void bindCategories(final List<CategoryInformation> model);

    void bindList(final ListInformation model);

    void onSuccessfulUpdate(final String name, final long categoryId);

    void onFailedUpdate();
}
