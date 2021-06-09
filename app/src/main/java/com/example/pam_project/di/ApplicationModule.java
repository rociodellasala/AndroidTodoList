package com.example.pam_project.di;

import android.content.Context;

import com.example.pam_project.db.daos.CategoryDao;
import com.example.pam_project.db.daos.ListDao;
import com.example.pam_project.db.daos.TaskDao;
import com.example.pam_project.db.mappers.CategoryMapper;
import com.example.pam_project.db.mappers.ListMapper;
import com.example.pam_project.db.repositories.CategoriesRepository;
import com.example.pam_project.db.repositories.ListsRepository;
import com.example.pam_project.db.repositories.RoomCategoriesRepository;
import com.example.pam_project.db.repositories.RoomListsRepository;
import com.example.pam_project.db.repositories.RoomTaskRepository;
import com.example.pam_project.db.repositories.TaskRepository;
import com.example.pam_project.db.utils.AppDatabase;
import com.example.pam_project.landing.FtuStorage;
import com.example.pam_project.landing.SharedPreferencesFtuStorage;

import static android.content.Context.MODE_PRIVATE;

public class ApplicationModule {
    private static final String PAM_PREF = "app-pref";

    private final Context applicationContext;
    private final AppDatabase mainStorage;

    /* default */ ApplicationModule(final Context context) {
        applicationContext = context.getApplicationContext();
        mainStorage = AppDatabase.getInstance(applicationContext);
    }

    /* default */ Context getApplicationContext() {
        return applicationContext;
    }

    /* default */ FtuStorage provideFtuStorage() {
        return new SharedPreferencesFtuStorage(
                applicationContext.getSharedPreferences(PAM_PREF, MODE_PRIVATE)
        );
    }

    /* default */ CategoriesRepository provideCategoriesRepository(final CategoryDao dao,
                                                                   final CategoryMapper mapper) {

        return new RoomCategoriesRepository(dao, mapper);
    }

    /* default */ CategoryDao provideCategoryDao() {
        return mainStorage.categoryDao();
    }

    /* default */ CategoryMapper provideCategoryMapper() {
        return new CategoryMapper();
    }

    /* default */ ListsRepository provideListsRepository(final ListDao listDao,
                                                         final CategoryDao categoryDao,
                                                         final ListMapper mapper) {

        return new RoomListsRepository(listDao, categoryDao, mapper);
    }

    /* default */ ListDao provideListDao() {
        return mainStorage.listDao();
    }

    /* default */ ListMapper provideListMapper() {
        return new ListMapper();
    }

    /* default */ TaskRepository provideTasksRepository(final TaskDao dao) {
        return new RoomTaskRepository(dao);
    }

    /* default */ TaskDao provideTaskDao() {
        return mainStorage.taskDao();
    }
}
