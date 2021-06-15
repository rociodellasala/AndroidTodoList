package com.example.pam_project.di;

import android.content.Context;

import com.example.pam_project.features.others.about.authors.AuthorsRepository;
import com.example.pam_project.features.others.about.version.VersionRepository;
import com.example.pam_project.landing.FtuStorage;
import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.example.pam_project.repositories.lists.ListsRepository;
import com.example.pam_project.repositories.tasks.TaskRepository;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

/**
 * Entidad encargada de contener las dependencias
 */
public interface ApplicationContainer {

    Context getApplicationContext();

    FtuStorage getFtuStorage();

    SchedulerProvider getSchedulerProvider();

    CategoriesRepository getCategoriesRepository();

    ListsRepository getListsRepository();

    TaskRepository getTasksRepository();

    AuthorsRepository getAuthorsRepository();

    VersionRepository getVersionRepository();
}
