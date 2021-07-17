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

    override var ftuStorage: FtuStorage? = null
        get() {
            if (field == null) {
                field = applicationModule.provideFtuStorage()
            }
            return field
        }
        private set

    override var schedulerProvider: SchedulerProvider? = null
        get() {
            if (field == null) {
                field = applicationModule.provideSchedulerProvider()
            }
            return field
        }
        private set

    override var categoriesRepository: CategoriesRepository? = null
        get() {
            if (field == null) {
                field = applicationModule.provideCategoriesRepository(
                        categoryDao, categoryMapper
                )
            }
            return field
        }
        private set

    private var categoryDao: CategoryDao? = null
         get() {
            if (field == null) {
                field = applicationModule.provideCategoryDao()
            }
            return field
        }

    private var categoryMapper: CategoryMapper? = null
         get() {
            if (field == null) {
                field = applicationModule.provideCategoryMapper()
            }
            return field
        }
    override var listsRepository: ListsRepository? = null
        get() {
            if (field == null) {
                field = applicationModule.provideListsRepository(
                        listDao, listMapper
                )
            }
            return field
        }
        private set

    private var listDao: ListDao? = null
         get() {
            if (field == null) {
                field = applicationModule.provideListDao()
            }
            return field
        }

    private var listMapper: ListMapper? = null
         get() {
            if (field == null) {
                field = applicationModule.provideListMapper()
            }
            return field
        }
    override var tasksRepository: TaskRepository? = null
        get() {
            if (field == null) {
                field = applicationModule.provideTasksRepository(taskDao, taskMapper)
            }
            return field
        }
        private set
    private var taskDao: TaskDao? = null
         get() {
            if (field == null) {
                field = applicationModule.provideTaskDao()
            }
            return field
        }
    private var taskMapper: TaskMapper? = null
         get() {
            if (field == null) {
                field = applicationModule.provideTaskMapper()
            }
            return field
        }

    private var service: APIServiceImplementation? = null
        get() {
            if (field == null) {
                field = applicationModule.provideAPIService()
            }
            return field
        }

    override var authorsRepository: AuthorsRepository? = null
        get() {
            if (field == null) {
                field = applicationModule.provideAuthorsRepository(service, authorsMapper)
            }
            return field
        }
        private set

    private var authorsMapper: AuthorsMapper? = null
        get() {
            if (field == null) {
                field = applicationModule.provideAuthorsMapper()
            }
            return field
        }

    override var versionRepository: VersionRepository? = null
        get() {
            if (field == null) {
                field = applicationModule.provideVersionRepository(service, versionMapper)
            }
            return field
        }
        private set

    private var versionMapper: VersionMapper? = null
        get() {
            if (field == null) {
                field = applicationModule.provideVersionMapper()
            }
            return field
        }

    override val applicationContext: Context
        get() = applicationModule.applicationContext
}