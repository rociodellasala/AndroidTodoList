package com.example.pam_project.repositories.categories

import com.example.pam_project.features.categories.list.CategoryInformation
import com.example.pam_project.features.lists.list.ListInformation
import io.reactivex.Completable
import io.reactivex.Flowable

interface CategoriesRepository {
    fun getCategory(id: Long): Flowable<CategoryInformation?>
    val categoriesWithLists: Flowable<Map<CategoryInformation?, List<ListInformation?>?>?>
    val categories: Flowable<List<CategoryInformation?>?>
    fun insertCategory(name: String, color: String): Completable
    fun updateCategory(id: Long, name: String, color: String): Completable
    fun deleteCategory(id: Long): Completable
}