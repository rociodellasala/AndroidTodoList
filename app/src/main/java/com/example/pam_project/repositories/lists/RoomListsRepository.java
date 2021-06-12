package com.example.pam_project.repositories.lists;

import com.example.pam_project.database.categories.CategoryDao;
import com.example.pam_project.database.categories.CategoryEntity;
import com.example.pam_project.database.lists.ListDao;
import com.example.pam_project.database.lists.ListEntity;
import com.example.pam_project.database.lists.ListMapper;
import com.example.pam_project.features.lists.list.ListInformation;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class RoomListsRepository implements ListsRepository {
    private final ListDao listDao;
    private final CategoryDao categoryDao;
    private final ListMapper mapper;

    public RoomListsRepository(final ListDao listDao, final CategoryDao categoryDao, final ListMapper mapper) {
        this.listDao = listDao;
        this.categoryDao = categoryDao;
        this.mapper = mapper;
    }

    @Override
    public ListInformation getList(final long listId) {
        ListEntity listEntity = listDao.getListById(listId).blockingFirst();
        CategoryEntity categoryEntity = categoryDao.getCategoryById(listEntity.categoryId).blockingFirst();
        return new ListInformation(listEntity.id, listEntity.name, listEntity.categoryId,
                categoryEntity.color);
    }

    @Override
    public Flowable<ListInformation> getListWithTasks(final long listId) {
        return listDao.getListsWithTasks(listId).map(mapper::toListWithTasksModel);
    }

    @Override
    public Completable insertList(final String name, final long categoryId) {
        ListEntity listEntity = new ListEntity(name, categoryId);
        return Completable.fromAction(() -> {
            listDao.insertList(listEntity);
        });
    }

    @Override
    public Completable updateList(final long id, final String name, final long categoryId) {
        ListEntity listEntity = new ListEntity(id, name, categoryId);
        return Completable.fromAction(() -> {
            listDao.updateList(listEntity);
        });
    }

    @Override
    public Completable deleteList(long id) {
        ListEntity listEntity = listDao.getListById(id).blockingFirst();
        return Completable.fromAction(() -> {
            listDao.deleteList(listEntity);
        });
    }
}


