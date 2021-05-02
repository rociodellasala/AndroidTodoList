package com.example.pam_project.db.repositories;

import com.example.pam_project.db.daos.CategoryDao;
import com.example.pam_project.db.daos.ListDao;
import com.example.pam_project.db.entities.CategoryEntity;
import com.example.pam_project.db.entities.ListEntity;
import com.example.pam_project.lists.lists.components.ListInformation;
import com.example.pam_project.utils.AppColor;
import com.example.pam_project.utils.ColorFinder;

public class RoomListsRepository implements ListsRepository {

    private final ListDao listDao;
    private final CategoryDao categoryDao;

    public RoomListsRepository(final ListDao listDao, final CategoryDao categoryDao){
        this.listDao = listDao;
        this.categoryDao = categoryDao;
    }

    @Override
    public ListInformation getList(final long listId){
        ListEntity listentity = listDao.getListById(listId).blockingFirst();
        CategoryEntity categoryEntity = categoryDao.getCategoryById(listentity.categoryId).blockingFirst();
        AppColor color = ColorFinder.findColor(categoryEntity.color);
        return new ListInformation(listentity.id, listentity.name, listentity.categoryId, color);
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


