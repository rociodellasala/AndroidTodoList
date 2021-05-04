package com.example.pam_project.db.repositories;

import com.example.pam_project.db.daos.CategoryDao;
import com.example.pam_project.db.daos.ListDao;
import com.example.pam_project.db.entities.CategoryEntity;
import com.example.pam_project.db.entities.ListEntity;
import com.example.pam_project.db.mappers.ListMapper;
import com.example.pam_project.lists.lists.components.ListInformation;
import com.example.pam_project.utils.AppColor;
import com.example.pam_project.utils.ColorFinder;

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
        AppColor color = ColorFinder.findColor(categoryEntity.color);
        return new ListInformation(listEntity.id, listEntity.name, listEntity.categoryId, color);
    }

    @Override
    public Flowable<ListInformation> getListWithTasks(final long listId) {
        return listDao.getListsWithTasks(listId).map(mapper::toListWithTasksModel);
    }

    @Override
    public long insertList(final String name, final long categoryId) {
        ListEntity listEntity = new ListEntity(name, categoryId);
        return listDao.insertList(listEntity);
    }

    @Override
    public void updateList(final long id, final String name, final long categoryId) {
        ListEntity listEntity = new ListEntity(id, name, categoryId);
        listDao.updateList(listEntity);
    }
}


