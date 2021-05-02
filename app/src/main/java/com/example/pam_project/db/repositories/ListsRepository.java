package com.example.pam_project.db.repositories;

import com.example.pam_project.lists.lists.components.ListInformation;

public interface ListsRepository {

    ListInformation getList(final long id);

    long insertList(final String name, final long categoryId);

    void updateList(final long id, final String name, final long categoryId);


}
