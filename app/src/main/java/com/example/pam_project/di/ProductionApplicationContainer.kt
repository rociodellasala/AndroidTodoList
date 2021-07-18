package com.example.pam_project.di

import android.content.Context
import com.example.pam_project.database.categories.CategoryDao
import com.example.pam_project.database.categories.CategoryMapper
import com.example.pam_project.database.lists.ListDao
import com.example.pam_project.database.lists.ListMapper
import com.example.pam_project.database.tasks.TaskDao
import com.example.pam_project.database.tasks.TaskMapper
import com.example.pam_project.landing.FtuStorage
import com.example.pam_project.networking.APIServiceImplementation
import com.example.pam_project.networking.authors.AuthorsMapper
import com.example.pam_project.networking.authors.AuthorsRepository
import com.example.pam_project.networking.version.VersionMapper
import com.example.pam_project.networking.version.VersionRepository
import com.example.pam_project.repositories.categories.CategoriesRepository
import com.example.pam_project.repositories.lists.ListsRepository
import com.example.pam_project.repositories.tasks.TaskRepository
import com.example.pam_project.utils.schedulers.SchedulerProvider

class ProductionApplicationContainer(context: Context) : ApplicationContainer {
    private val applicationModule: ApplicationModule = ApplicationModule(context)

    override var ftuStorage: FtuStorage = applicationModule.provideFtuStorage()

    override var schedulerProvider: SchedulerProvider = applicationModule.provideSchedulerProvider()

    private var categoryMapper: CategoryMapper = applicationModule.provideCategoryMapper()

    private var categoryDao: CategoryDao = applicationModule.provideCategoryDao()

    override var categoriesRepository: CategoriesRepository = applicationModule.provideCategoriesRepository(
        categoryDao, categoryMapper
    )

    private var listDao: ListDao = applicationModule.provideListDao()

    private var listMapper: ListMapper  = applicationModule.provideListMapper()

    override var listsRepository: ListsRepository = applicationModule.provideListsRepository(
        listDao, listMapper
    )

    private var taskDao: TaskDao = applicationModule.provideTaskDao()

    private var taskMapper: TaskMapper = applicationModule.provideTaskMapper()

    override var tasksRepository: TaskRepository = applicationModule.provideTasksRepository(taskDao, taskMapper)

    private var service: APIServiceImplementation = applicationModule.provideAPIService()

    private var authorsMapper: AuthorsMapper = applicationModule.provideAuthorsMapper()

    override var authorsRepository: AuthorsRepository = applicationModule.provideAuthorsRepository(service, authorsMapper)

    private var versionMapper: VersionMapper = applicationModule.provideVersionMapper()

    override var versionRepository: VersionRepository = applicationModule.provideVersionRepository(service, versionMapper)

    override val applicationContext: Context
        get() = applicationModule.applicationContext
}