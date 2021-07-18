package com.example.pam_project.repositories.categories

import com.example.pam_project.database.categories.CategoryDao
import com.example.pam_project.database.categories.CategoryEntity
import com.example.pam_project.database.categories.CategoryMapper
import com.example.pam_project.database.relationships.CategoriesWithLists
import com.example.pam_project.features.categories.list.CategoryInformation
import com.example.pam_project.features.lists.list.ListInformation
import io.reactivex.Completable
import io.reactivex.Flowable

class RoomCategoriesRepository(private val categoryDao: CategoryDao, private val mapper: CategoryMapper) : CategoriesRepository {
    override fun getCategory(id: Long): Flowable<CategoryInformation> {
        return categoryDao.getCategoryById(id).map { entity: CategoryEntity -> mapper.toModel(entity) }
    }

    override val categoriesWithLists: Flowable<Map<CategoryInformation, List<ListInformation>>>
        get() = categoryDao.allCategoriesWithLists.map { entities: List<CategoriesWithLists?>?
            -> mapper.toListWithCategoriesModel(entities) }

    override val categories: Flowable<List<CategoryInformation>>
        get() = categoryDao.allCategories.map { categoryEntities: List<CategoryEntity?>? -> mapper.toCategoryModel(categoryEntities) }

    override fun insertCategory(name: String, color: String): Completable {
        val entity = CategoryEntity(name, color)
        return Completable.fromAction { categoryDao.insertCategory(entity) }
    }

    override fun updateCategory(id: Long, name: String, color: String): Completable {
        val entity = CategoryEntity(id, name, color)
        return Completable.fromAction { categoryDao.updateCategory(entity) }
    }

    override fun deleteCategory(id: Long): Completable {
        val entity = categoryDao.getCategoryById(id).blockingFirst()
        return Completable.fromAction { categoryDao.deleteCategory(entity) }
    }
}