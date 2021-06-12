package com.example.pam_project.database.tasks;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import io.reactivex.Flowable;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM Tasks WHERE id =:id")
    Flowable<TaskEntity> getTaskById(final long id);

    @Insert
    void insertTask(final TaskEntity task);

    @Update
    void updateTask(final TaskEntity task);

    @Delete
    void deleteTask(final TaskEntity task);
}