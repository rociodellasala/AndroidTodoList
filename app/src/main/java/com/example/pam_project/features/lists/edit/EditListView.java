package com.example.pam_project.features.lists.edit;

import com.example.pam_project.features.categories.list.CategoryInformation;
import com.example.pam_project.features.lists.list.ListInformation;

import java.util.List;

public interface EditListView {

    void bindCategories(final List<CategoryInformation> model);

    void bindList(final ListInformation model);

    void onListDelete();

    void onListDeletedError();

    void onListUpdatedError();

    void onCategoriesReceivedError();

    void showDeleteDialog();
}
