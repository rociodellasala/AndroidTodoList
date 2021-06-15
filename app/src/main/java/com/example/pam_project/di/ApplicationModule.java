package com.example.pam_project.di;

import android.content.Context;

import com.example.pam_project.database.categories.CategoryDao;
import com.example.pam_project.database.categories.CategoryMapper;
import com.example.pam_project.database.lists.ListDao;
import com.example.pam_project.database.lists.ListMapper;
import com.example.pam_project.database.tasks.TaskDao;
import com.example.pam_project.database.utils.AppDatabase;
import com.example.pam_project.landing.FtuStorage;
import com.example.pam_project.landing.SharedPreferencesFtuStorage;
import com.example.pam_project.networking.APIServiceImplementation;
import com.example.pam_project.networking.authors.AuthorsMapper;
import com.example.pam_project.networking.authors.AuthorsRepository;
import com.example.pam_project.networking.authors.RestAuthorsRepository;
import com.example.pam_project.networking.version.RestVersionRepository;
import com.example.pam_project.networking.version.VersionMapper;
import com.example.pam_project.networking.version.VersionRepository;
import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.example.pam_project.repositories.categories.RoomCategoriesRepository;
import com.example.pam_project.repositories.lists.ListsRepository;
import com.example.pam_project.repositories.lists.RoomListsRepository;
import com.example.pam_project.repositories.tasks.RoomTaskRepository;
import com.example.pam_project.repositories.tasks.TaskRepository;
import com.example.pam_project.utils.schedulers.AndroidSchedulerProvider;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

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

    /* default */ SchedulerProvider provideSchedulerProvider() {
        return new AndroidSchedulerProvider();
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

    /* default */ AuthorsRepository provideAuthorsRepository() {
        return new RestAuthorsRepository(new APIServiceImplementation(), new AuthorsMapper());
    }

    /* default */ VersionRepository provideVersionRepository() {
        return new RestVersionRepository(new APIServiceImplementation(), new VersionMapper());
    }
}
