package com.example.pam_project.features.lists.list;

import com.example.pam_project.features.categories.list.CategoryInformation;

import java.util.List;

public interface ListView {

    void launchFtu();

    void bindCategories(final List<CategoryInformation> model);

    void bindLists(final List<ListInformation> model);

    void showAddList();

    void showSearchBar();

    void showFilterDialog();

    void showSortByDialog();

    void showManageCategories();

    void showListContent(final long id);

    void showEmptyMessage();
}
