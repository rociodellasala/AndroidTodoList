package com.example.pam_project.database.categories;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.pam_project.database.relationships.CategoriesWithLists;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM Categories")
    Flowable<List<CategoryEntity>> getAllCategories();

    @Query("SELECT * FROM Categories WHERE id =:id")
    Flowable<CategoryEntity> getCategoryById(long id);

    @Insert
    void insertCategory(final CategoryEntity category);

    @Update
    void updateCategory(final CategoryEntity category);

    @Delete
    void deleteCategory(final CategoryEntity category);

    @Transaction
    @Query("SELECT * FROM Categories")
    Flowable<List<CategoriesWithLists>> getAllCategoriesWithLists();
}