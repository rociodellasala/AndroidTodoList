package com.example.pam_project.features.lists.list;

import com.example.pam_project.features.categories.list.CategoryInformation;

import java.util.List;

public interface ListView {

    void launchFtu();

    void showLists();

    void bindCategories(List<CategoryInformation> model);

    void bindLists(final List<ListInformation> model);

    void bindList(final ListInformation model);

    void showAddList();

    void showSearchBar();

    void showFilterDialog();

    void showSortByDialog();

    void showManageCategories();

    void showListContent(final long id);

    void showEmptyMessage();
}
