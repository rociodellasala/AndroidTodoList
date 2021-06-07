package com.example.pam_project.di;

import android.content.Context;

import com.example.pam_project.db.daos.CategoryDao;
import com.example.pam_project.db.daos.ListDao;
import com.example.pam_project.db.daos.TaskDao;
import com.example.pam_project.db.mappers.CategoryMapper;
import com.example.pam_project.db.mappers.ListMapper;
import com.example.pam_project.db.repositories.CategoriesRepository;
import com.example.pam_project.db.repositories.ListsRepository;
import com.example.pam_project.db.repositories.TaskRepository;
import com.example.pam_project.landing.FtuStorage;

public class ProductionApplicationContainer implements ApplicationContainer {

    private final ApplicationModule applicationModule;

    private FtuStorage ftuStorage;
    private CategoriesRepository categoriesRepository;
    private CategoryDao categoryDao;
    private CategoryMapper categoryMapper;

    private ListsRepository listsRepository;
    private ListDao listDao;
    private ListMapper listMapper;

    private TaskRepository tasksRepository;
    private TaskDao taskDao;

    public ProductionApplicationContainer(final Context context) {
        applicationModule = new ApplicationModule(context);
    }

    @Override
    public Context getApplicationContext() {
        return applicationModule.getApplicationContext();
    }

    @Override
    public FtuStorage getFtuStorage() {
        if (ftuStorage == null) {
            ftuStorage = applicationModule.provideFtuStorage();
        }

        return ftuStorage;
    }

    @Override
    public CategoriesRepository getCategoriesRepository() {
        if (categoriesRepository == null) {
            categoriesRepository = applicationModule.provideCategoriesRepository(
                    getCategoryDao(), getCategoryMapper()
            );
        }

        return categoriesRepository;
    }

    @Override
    public ListsRepository getListsRepository() {
        if (listsRepository == null) {
            listsRepository = applicationModule.provideListsRepository(
                    getListDao(), getCategoryDao(), getListMapper()
            );
        }

        return listsRepository;
    }

    @Override
    public TaskRepository getTasksRepository() {
        if (tasksRepository == null) {
            tasksRepository = applicationModule.provideTasksRepository(getTaskDao());
        }

        return tasksRepository;
    }

    private CategoryDao getCategoryDao() {
        if (categoryDao == null) {
            categoryDao = applicationModule.provideCategoryDao();
        }

        return categoryDao;
    }

    private CategoryMapper getCategoryMapper() {
        if (categoryMapper == null) {
            categoryMapper = applicationModule.provideCategoryMapper();
        }

        return categoryMapper;
    }

    private ListDao getListDao() {
        if (listDao == null) {
            listDao = applicationModule.provideListDao();
        }

        return listDao;
    }

    private ListMapper getListMapper() {
        if (listMapper == null) {
            listMapper = applicationModule.provideListMapper();
        }

        return listMapper;
    }

    private TaskDao getTaskDao() {
        if (taskDao == null) {
            taskDao = applicationModule.provideTaskDao();
        }

        return taskDao;
    }
}
