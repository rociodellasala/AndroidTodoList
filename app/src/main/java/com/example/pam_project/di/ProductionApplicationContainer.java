package com.example.pam_project.di;

import android.content.Context;

import com.example.pam_project.database.categories.CategoryDao;
import com.example.pam_project.database.categories.CategoryMapper;
import com.example.pam_project.database.lists.ListDao;
import com.example.pam_project.database.lists.ListMapper;
import com.example.pam_project.database.tasks.TaskDao;
import com.example.pam_project.database.tasks.TaskMapper;
import com.example.pam_project.landing.FtuStorage;
import com.example.pam_project.networking.APIServiceImplementation;
import com.example.pam_project.networking.authors.AuthorsMapper;
import com.example.pam_project.networking.authors.AuthorsRepository;
import com.example.pam_project.networking.version.VersionMapper;
import com.example.pam_project.networking.version.VersionRepository;
import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.example.pam_project.repositories.lists.ListsRepository;
import com.example.pam_project.repositories.tasks.TaskRepository;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

public class ProductionApplicationContainer implements ApplicationContainer {

    private final ApplicationModule applicationModule;

    private FtuStorage ftuStorage;
    private SchedulerProvider schedulerProvider;

    private CategoriesRepository categoriesRepository;
    private CategoryDao categoryDao;
    private CategoryMapper categoryMapper;

    private ListsRepository listsRepository;
    private ListDao listDao;
    private ListMapper listMapper;

    private TaskRepository tasksRepository;
    private TaskDao taskDao;
    private TaskMapper taskMapper;

    private APIServiceImplementation service;

    private AuthorsRepository authorsRepository;
    private AuthorsMapper authorsMapper;
    private VersionRepository versionRepository;
    private VersionMapper versionMapper;


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
    public SchedulerProvider getSchedulerProvider() {
        if (schedulerProvider == null) {
            schedulerProvider = applicationModule.provideSchedulerProvider();
        }

        return schedulerProvider;
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
            tasksRepository = applicationModule.provideTasksRepository(getTaskDao(), getTaskMapper());
        }

        return tasksRepository;
    }

    @Override
    public AuthorsRepository getAuthorsRepository() {
        if (authorsRepository == null) {
            authorsRepository = applicationModule.provideAuthorsRepository(getService(), getAuthorsMapper());
        }

        return authorsRepository;
    }

    private APIServiceImplementation getService() {
        if (service == null) {
            service = applicationModule.provideAPIService();
        }

        return service;
    }

    private AuthorsMapper getAuthorsMapper() {
        if (authorsMapper == null) {
            authorsMapper = applicationModule.provideAuthorsMapper();
        }

        return authorsMapper;
    }

    @Override
    public VersionRepository getVersionRepository() {
        if (versionRepository == null) {
            versionRepository = applicationModule.provideVersionRepository(getService(), getVersionMapper());
        }

        return versionRepository;
    }

    private VersionMapper getVersionMapper() {
        if (versionMapper == null) {
            versionMapper = applicationModule.provideVersionMapper();
        }

        return versionMapper;
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

    private TaskMapper getTaskMapper() {
        if (taskMapper == null) {
            taskMapper = applicationModule.provideTaskMapper();
        }

        return taskMapper;
    }
}
