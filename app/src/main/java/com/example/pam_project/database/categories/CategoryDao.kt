package com.example.pam_project.database.categories

import androidx.room.*
import com.example.pam_project.database.relationships.CategoriesWithLists
import io.reactivex.Flowable

@Dao
interface CategoryDao {
    @get:Query("SELECT * FROM Categories")
    val allCategories: Flowable<List<CategoryEntity?>?>

    @Query("SELECT * FROM Categories WHERE id =:id")
    fun getCategoryById(id: Long): Flowable<CategoryEntity?>

    @Insert
    fun insertCategory(category: CategoryEntity?)

    @Update
    fun updateCategory(category: CategoryEntity?)

    @Delete
    fun deleteCategory(category: CategoryEntity?)

    @get:Query("SELECT * FROM Categories")
    @get:Transaction
    val allCategoriesWithLists: Flowable<List<CategoriesWithLists?>?>
}