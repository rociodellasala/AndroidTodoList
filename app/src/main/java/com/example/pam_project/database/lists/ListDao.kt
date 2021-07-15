package com.example.pam_project.database.lists

import androidx.room.*
import com.example.pam_project.database.relationships.ListsWithTasks
import io.reactivex.Flowable

@Dao
interface ListDao {
    @Query("SELECT * FROM Lists WHERE id =:id")
    fun getListById(id: Long): Flowable<ListEntity?>

    @Insert
    fun insertList(list: ListEntity?)

    @Update
    fun updateList(list: ListEntity?)

    @Delete
    fun deleteList(list: ListEntity?)

    @Transaction
    @Query("SELECT * FROM Lists WHERE id =:id")
    fun getListsWithTasks(id: Long): Flowable<ListsWithTasks?>
}