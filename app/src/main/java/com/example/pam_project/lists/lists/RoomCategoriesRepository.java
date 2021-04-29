package com.example.pam_project.lists.lists;

import com.example.pam_project.db.daos.CategoryDao;
import com.example.pam_project.db.relationships.CategoriesWithLists;

import java.util.List;

import io.reactivex.Flowable;

public class RoomCategoriesRepository implements CategoriesRepository{

    private final CategoryDao categoryDao;

    public RoomCategoriesRepository(final CategoryDao categoryDao){
        this.categoryDao = categoryDao;
    }

    @Override
    public Flowable<List<CategoriesWithLists>> getCategoriesWithLists() {
        return categoryDao.getAllCategoriesWithLists();
    }
}
