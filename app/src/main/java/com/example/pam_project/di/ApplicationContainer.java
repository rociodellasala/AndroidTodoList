package com.example.pam_project.di;

import android.content.Context;

import com.example.pam_project.db.repositories.CategoriesRepository;
import com.example.pam_project.db.repositories.ListsRepository;
import com.example.pam_project.db.repositories.TaskRepository;
import com.example.pam_project.landing.FtuStorage;

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
