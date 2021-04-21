package com.example.pam_project.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.pam_project.db.entities.CategoryEntity;
import com.example.pam_project.db.relationships.CategoriesWithLists;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM Categories")
    Flowable<List<CategoryEntity>> getAllCategories();

    @Query("SELECT * FROM Categories WHERE id =:id")
    CategoryEntity getCategoryById(long id);

    @Insert
    long insertCategory(final CategoryEntity category);

    @Insert
    long[] insertAllCategories(final List<CategoryEntity> categories);

    @Update
    void updateCategory(final CategoryEntity category);

    @Delete
    void deleteCategory(final CategoryEntity category);

    @Query("DELETE FROM Categories")
    void deleteAllCategories();

    @Transaction
    @Query("SELECT * FROM Categories WHERE id =:id")
    Flowable<List<CategoriesWithLists>> getCategoriesWithLists(long id);

    @Transaction
    @Query("SELECT * FROM Categories")
    Flowable<List<CategoriesWithLists>> getAllCategoriesWithLists();


}