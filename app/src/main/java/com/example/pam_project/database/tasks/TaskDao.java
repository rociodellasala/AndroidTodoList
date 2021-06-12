package com.example.pam_project.database.tasks;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM Tasks")
    Flowable<List<TaskEntity>> getAllTasks();

    @Query("SELECT * FROM Tasks WHERE id =:id")
    Flowable<TaskEntity> getTaskById(final long id);

    @Insert
    long insertTask(final TaskEntity task);

    @Insert
    void insertAllTasks(final List<TaskEntity> tasks);

    @Update
    void updateTask(final TaskEntity task);

    @Delete
    void deleteTask(final TaskEntity task);

    @Query("DELETE FROM Tasks")
    void deleteAllTasks();
}