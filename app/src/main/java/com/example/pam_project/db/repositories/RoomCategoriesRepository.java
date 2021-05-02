package com.example.pam_project.db.repositories;

import com.example.pam_project.db.daos.CategoryDao;
import com.example.pam_project.db.mappers.CategoryMapper;
import com.example.pam_project.lists.categories.CategoryInformation;
import com.example.pam_project.lists.lists.components.ListInformation;

import java.util.List;

import io.reactivex.Flowable;

public class RoomCategoriesRepository implements CategoriesRepository {

    private final CategoryDao categoryDao;
    private final CategoryMapper mapper;

    public RoomCategoriesRepository(final CategoryDao categoryDao, final CategoryMapper mapper){
        this.categoryDao = categoryDao;
        this.mapper = mapper;
    }

    @Override
    public Flowable<CategoryInformation> getCategory(final long id){
        return categoryDao.getCategoryById(id).map(mapper::toModel);
    }

    @Override
    public Flowable<List<ListInformation>> getCategoriesWithLists() {
        return categoryDao.getAllCategoriesWithLists().map(mapper::toListModel);
    }

    @Override
    public Flowable<List<CategoryInformation>> getCategories() {
        return categoryDao.getAllCategories().map(mapper::toCategoryModel);
    }
}
