package com.example.pam_project.db.repositories;

import com.example.pam_project.db.daos.CategoryDao;
import com.example.pam_project.db.daos.ListDao;
import com.example.pam_project.db.entities.CategoryEntity;
import com.example.pam_project.db.entities.ListEntity;
import com.example.pam_project.db.mappers.CategoryMapper;
import com.example.pam_project.db.mappers.ListMapper;
import com.example.pam_project.lists.lists.components.ListInformation;
import com.example.pam_project.utils.AppColor;

import java.util.Arrays;
import java.util.List;

public class RoomListsRepository implements ListsRepository {

    private final ListDao listDao;
    private final CategoryDao categoryDao;
    private final ListMapper listMapper;
    private final CategoryMapper categoryMapper;

    public RoomListsRepository(final ListDao listDao, final CategoryDao categoryDao,
                               final ListMapper listMapper, final CategoryMapper categoryMapper){
        this.listDao = listDao;
        this.categoryDao = categoryDao;
        this.listMapper = listMapper;
        this.categoryMapper = categoryMapper;
    }

    public ListInformation getList(final long listId){
        ListEntity listentity = listDao.getListById(listId).blockingFirst();
        CategoryEntity categoryEntity = categoryDao.getCategoryById(listentity.categoryId).blockingFirst();
        AppColor color = findColor(categoryEntity.color);
        return new ListInformation(listentity.id, listentity.name, listentity.categoryId, color);
    }

    private static AppColor findColor(String color) {
        final List<AppColor> colors = Arrays.asList(AppColor.values());

        for (int i = 0; i < colors.size(); i++) {
            if (color.equals(colors.get(i).toString())) {
                return colors.get(i);
            }
        }

        return null;
    }
}


