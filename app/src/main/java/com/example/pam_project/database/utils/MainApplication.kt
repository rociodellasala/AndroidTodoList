package com.example.pam_project.database.utils

import android.app.Application
import android.content.Context
import com.example.pam_project.R
import com.example.pam_project.database.categories.CategoryEntity
import com.example.pam_project.di.ApplicationContainerLocator
import com.example.pam_project.utils.constants.AppColor
import io.reactivex.Completable

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val container = ApplicationContainerLocator.locateComponent(this)
        val schedulerProvider = container?.schedulerProvider
        val ftuStorage = container?.ftuStorage
        if (ftuStorage!!.isActive) {
            val appContext = applicationContext
            val db: AppDatabase? = AppDatabase.getInstance(appContext)
            Completable.fromAction {
                db?.categoryDao()?.insertCategory(createDefaultCategory(appContext))
            }.onErrorComplete()
                    .subscribeOn(schedulerProvider!!.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe()
        }
    }

    private fun createDefaultCategory(context: Context): CategoryEntity {
        val defaultCategory = context.resources.getString(R.string.default_category)
        val colors = listOf(*AppColor.values())
        return CategoryEntity(defaultCategory, colors[0].toString())
    }
}