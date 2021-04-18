package com.example.pam_project.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.pam_project.db.entities.ListEntity;
import com.example.pam_project.db.relationships.ListsWithTasks;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ListDao {
    @Query("SELECT * FROM Lists")
    Flowable<List<ListEntity>> getAllLists();

    @Query("SELECT * FROM Lists WHERE id =:id")
    Flowable<ListEntity> getListById(int id);

    @Insert
    long insertList(final ListEntity list);

    @Insert
    long[] insertAllLists(final List<ListEntity> lists);

    @Update
    void updateList(final ListEntity category);

    @Delete
    void deleteList(final ListEntity list);

    @Query("DELETE FROM Lists")
    void deleteAllLists();

    @Transaction
    @Query("SELECT * FROM Lists WHERE id =:id")
    Flowable<ListsWithTasks> getListsWithTasks(int id);
}
