package com.example.pam_project.db.repositories;

import com.example.pam_project.db.daos.CategoryDao;
import com.example.pam_project.db.entities.CategoryEntity;
import com.example.pam_project.db.entities.ListEntity;
import com.example.pam_project.db.mappers.CategoryMapper;
import com.example.pam_project.lists.categories.components.CategoryInformation;
import com.example.pam_project.lists.lists.components.ListInformation;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

public class RoomCategoriesRepository implements CategoriesRepository {

    private final CategoryDao categoryDao;
    private final CategoryMapper mapper;

    public RoomCategoriesRepository(final CategoryDao categoryDao, final CategoryMapper mapper) {
        this.categoryDao = categoryDao;
        this.mapper = mapper;
    }

    @Override
    public CategoryInformation getCategory(final long id) {
        return categoryDao.getCategoryById(id).map(mapper::toModel).blockingFirst();
    }

    @Override
    public Flowable<Map<CategoryInformation, List<ListInformation>>> getCategoriesWithLists() {
        return categoryDao.getAllCategoriesWithLists().map(mapper::toListWithCategoriesModel);
    }

    @Override
    public Flowable<List<CategoryInformation>> getCategories() {
        return categoryDao.getAllCategories().map(mapper::toCategoryModel);
    }

    @Override
    public long insertCategory(final String name, final String color) {
        CategoryEntity entity = new CategoryEntity(name, color);
        return categoryDao.insertCategory(entity);
    }

    @Override
    public void updateCategory(final long id, final String name, final String color) {
        CategoryEntity entity = new CategoryEntity(id, name, color);
        categoryDao.updateCategory(entity);
    }
}
