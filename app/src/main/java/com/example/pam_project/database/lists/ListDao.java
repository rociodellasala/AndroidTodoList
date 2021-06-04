package com.example.pam_project.database.lists;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.pam_project.database.relationships.ListsWithTasks;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ListDao {
    @Query("SELECT * FROM Lists")
    Flowable<List<ListEntity>> getAllLists();

    @Query("SELECT * FROM Lists WHERE id =:id")
    Flowable<ListEntity> getListById(final long id);

    @Insert
    long insertList(final ListEntity list);

    @Insert
    long[] insertAllLists(final List<ListEntity> lists);

    @Update
    void updateList(final ListEntity list);

    @Delete
    void deleteList(final ListEntity list);

    @Query("DELETE FROM Lists")
    void deleteAllLists();

    @Transaction
    @Query("SELECT * FROM Lists WHERE id =:id")
    Flowable<ListsWithTasks> getListsWithTasks(final long id);
}