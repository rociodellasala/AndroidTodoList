package com.example.pam_project.di;

import android.content.Context;

import com.example.pam_project.landing.FtuStorage;
import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.example.pam_project.repositories.lists.ListsRepository;
import com.example.pam_project.repositories.tasks.TaskRepository;

/**
 * Entidad encargada de contener las dependencias
 */
public interface ApplicationContainer {

    Context getApplicationContext();

    FtuStorage getFtuStorage();

    CategoriesRepository getCategoriesRepository();

    ListsRepository getListsRepository();

    TaskRepository getTasksRepository();
}
