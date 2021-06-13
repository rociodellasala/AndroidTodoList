package com.example.pam_project.features.lists.list;

import com.example.pam_project.features.categories.list.CategoryInformation;

import java.util.List;

public interface ListView {

    void launchFtu();

    void bindCategories(List<CategoryInformation> model);

    void bindLists(final List<ListInformation> model);

    void showAddList();

    void bindSearchedLists(String searchQuery);

    void unbindSearchedLists();

    void showFilterDialog();

    void showSortByDialog();

    void showManageCategories();

    void showListContent(final long id);

    void showEmptyMessage();

    void onListsReceivedError();

    void onCategoriesReceivedError();
}
