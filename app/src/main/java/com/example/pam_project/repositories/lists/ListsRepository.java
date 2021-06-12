package com.example.pam_project.repositories.lists;

import com.example.pam_project.features.lists.list.ListInformation;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface ListsRepository {

    ListInformation getList(final long id);

    Completable insertList(final String name, final long categoryId);

    Completable updateList(final long id, final String name, final long categoryId);

    Completable deleteList(final long id);

    Flowable<ListInformation> getListWithTasks(final long listId);

}
