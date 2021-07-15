package com.example.pam_project.database.tasks

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface TaskDao {
    @Query("SELECT * FROM Tasks WHERE id =:id")
    fun getTaskById(id: Long): Flowable<TaskEntity?>

    @Insert
    fun insertTask(task: TaskEntity?)

    @Update
    fun updateTask(task: TaskEntity?)

    @Delete
    fun deleteTask(task: TaskEntity?)
}