package com.example.pam_project.lists.lists;

import com.example.pam_project.db.relationships.CategoriesWithLists;

import java.util.List;

public interface ListView {

    void launchFtu();

    void showLists();

    void bindLists(final List<ListInformation> model);

    void showListContent(final long id);
}
