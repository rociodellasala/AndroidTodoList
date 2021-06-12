package com.example.pam_project.database.lists;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.pam_project.database.relationships.ListsWithTasks;

import io.reactivex.Flowable;

@Dao
public interface ListDao {
    @Query("SELECT * FROM Lists WHERE id =:id")
    Flowable<ListEntity> getListById(final long id);

    @Insert
    void insertList(final ListEntity list);

    @Update
    void updateList(final ListEntity list);

    @Delete
    void deleteList(final ListEntity list);

    @Transaction
    @Query("SELECT * FROM Lists WHERE id =:id")
    Flowable<ListsWithTasks> getListsWithTasks(final long id);
}