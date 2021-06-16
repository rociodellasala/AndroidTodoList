package com.example.pam_project.repositories.categories;

import com.example.pam_project.database.categories.CategoryDao;
import com.example.pam_project.database.categories.CategoryEntity;
import com.example.pam_project.database.categories.CategoryMapper;
import com.example.pam_project.features.categories.list.CategoryInformation;
import com.example.pam_project.features.lists.list.ListInformation;

import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class RoomCategoriesRepository implements CategoriesRepository {
    private final CategoryDao categoryDao;
    private final CategoryMapper mapper;

    public RoomCategoriesRepository(final CategoryDao categoryDao, final CategoryMapper mapper) {
        this.categoryDao = categoryDao;
        this.mapper = mapper;
    }

    @Override
    public Flowable<CategoryInformation> getCategory(final long id) {
        return categoryDao.getCategoryById(id).map(mapper::toModel);
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
    public Completable insertCategory(final String name, final String color) {
        CategoryEntity entity = new CategoryEntity(name, color);
        return Completable.fromAction(() -> categoryDao.insertCategory(entity));
    }

    @Override
    public Completable updateCategory(final long id, final String name, final String color) {
        CategoryEntity entity = new CategoryEntity(id, name, color);
        return Completable.fromAction(() -> categoryDao.updateCategory(entity));
    }

    @Override
    public Completable deleteCategory(final long id) {
        CategoryEntity entity = categoryDao.getCategoryById(id).blockingFirst();
        return Completable.fromAction(() -> categoryDao.deleteCategory(entity));
    }
}
