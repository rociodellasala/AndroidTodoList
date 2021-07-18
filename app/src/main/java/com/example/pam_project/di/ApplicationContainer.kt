package com.example.pam_project.di

import android.content.Context
import com.example.pam_project.landing.FtuStorage
import com.example.pam_project.networking.authors.AuthorsRepository
import com.example.pam_project.networking.version.VersionRepository
import com.example.pam_project.repositories.categories.CategoriesRepository
import com.example.pam_project.repositories.lists.ListsRepository
import com.example.pam_project.repositories.tasks.TaskRepository
import com.example.pam_project.utils.schedulers.SchedulerProvider

/**
 * Entidad encargada de contener las dependencias
 */
interface ApplicationContainer {
    val applicationContext: Context
    val ftuStorage: FtuStorage
    val schedulerProvider: SchedulerProvider
    val categoriesRepository: CategoriesRepository
    val listsRepository: ListsRepository
    val tasksRepository: TaskRepository
    val authorsRepository: AuthorsRepository
    val versionRepository: VersionRepository
}