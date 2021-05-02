package com.example.pam_project.lists.lists.listActivity;

import com.example.pam_project.lists.lists.components.ListInformation;

import java.util.List;

public interface ListView {

    void launchFtu();

    void showLists();

    void bindLists(final List<ListInformation> model);

    void bindList(final ListInformation model);

    void showAddList();

    void showSearchBar();

    void showFilterDialog();

    void showSortByDialog();

    void showManageCategories();

    void showListContent(final long id);
}
