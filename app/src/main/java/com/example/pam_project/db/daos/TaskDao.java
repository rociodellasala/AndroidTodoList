package com.example.pam_project.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pam_project.db.entities.TaskEntity;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM Tasks")
    Flowable<List<TaskEntity>> getAllTasks();

    @Query("SELECT * FROM Tasks WHERE id =:id")
    TaskEntity getTaskById(long id);

    @Insert
    long insertTask(final TaskEntity task);

    @Insert
    long[] insertAllTasks(final List<TaskEntity> tasks);

    @Update
    void updateTask(final TaskEntity task);

    @Delete
    void deleteTask(final TaskEntity task);

    @Query("DELETE FROM Tasks")
    public void deleteAllTasks();
}