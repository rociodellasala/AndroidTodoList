package com.example.pam_project.di

import android.content.Context
import com.example.pam_project.database.categories.CategoryDao
import com.example.pam_project.database.categories.CategoryMapper
import com.example.pam_project.database.lists.ListDao
import com.example.pam_project.database.lists.ListMapper
import com.example.pam_project.database.tasks.TaskDao
import com.example.pam_project.database.tasks.TaskMapper
import com.example.pam_project.database.utils.AppDatabase
import com.example.pam_project.landing.FtuStorage
import com.example.pam_project.landing.SharedPreferencesFtuStorage
import com.example.pam_project.networking.APIServiceImplementation
import com.example.pam_project.networking.authors.AuthorsMapper
import com.example.pam_project.networking.authors.AuthorsRepository
import com.example.pam_project.networking.authors.RestAuthorsRepository
import com.example.pam_project.networking.version.RestVersionRepository
import com.example.pam_project.networking.version.VersionMapper
import com.example.pam_project.networking.version.VersionRepository
import com.example.pam_project.repositories.categories.CategoriesRepository
import com.example.pam_project.repositories.categories.RoomCategoriesRepository
import com.example.pam_project.repositories.lists.ListsRepository
import com.example.pam_project.repositories.lists.RoomListsRepository
import com.example.pam_project.repositories.tasks.RoomTaskRepository
import com.example.pam_project.repositories.tasks.TaskRepository
import com.example.pam_project.utils.schedulers.AndroidSchedulerProvider
import com.example.pam_project.utils.schedulers.SchedulerProvider

class ApplicationModule internal constructor(context: Context) {
    /* default */  val applicationContext: Context = context.applicationContext
    private val mainStorage: AppDatabase = AppDatabase.getInstance(applicationContext)!!

    /* default */
    fun provideFtuStorage(): FtuStorage {
        return SharedPreferencesFtuStorage(applicationContext.getSharedPreferences(PAM_PREF, Context.MODE_PRIVATE))
    }

    /* default */
    fun provideSchedulerProvider(): SchedulerProvider {
        return AndroidSchedulerProvider()
    }

    /* default */
    fun provideCategoriesRepository(dao: CategoryDao, mapper: CategoryMapper): CategoriesRepository {
        return RoomCategoriesRepository(dao, mapper)
    }

    /* default */
    fun provideCategoryDao(): CategoryDao {
        return mainStorage.categoryDao()
    }

    /* default */
    fun provideCategoryMapper(): CategoryMapper {
        return CategoryMapper()
    }

    /* default */
    fun provideListsRepository(listDao: ListDao, mapper: ListMapper): ListsRepository {
        return RoomListsRepository(listDao, mapper)
    }

    /* default */
    fun provideListDao(): ListDao {
        return mainStorage.listDao()
    }

    /* default */
    fun provideListMapper(): ListMapper {
        return ListMapper()
    }

    /* default */
    fun provideTasksRepository(dao: TaskDao, mapper: TaskMapper): TaskRepository {
        return RoomTaskRepository(dao, mapper)
    }

    /* default */
    fun provideTaskDao(): TaskDao {
        return mainStorage.taskDao()
    }

    /* default */
    fun provideTaskMapper(): TaskMapper {
        return TaskMapper()
    }

    /* default */
    fun provideAuthorsRepository(service: APIServiceImplementation?, mapper: AuthorsMapper?): AuthorsRepository {
        return RestAuthorsRepository(service, mapper)
    }

    /* default */
    fun provideAuthorsMapper(): AuthorsMapper {
        return AuthorsMapper()
    }

    /* default */
    fun provideAPIService(): APIServiceImplementation {
        return APIServiceImplementation()
    }

    /* default */
    fun provideVersionRepository(service: APIServiceImplementation?, mapper: VersionMapper?): VersionRepository {
        return RestVersionRepository(service, mapper)
    }

    /* default */
    fun provideVersionMapper(): VersionMapper {
        return VersionMapper()
    }

    companion object {
        private const val PAM_PREF = "app-pref"
    }

}