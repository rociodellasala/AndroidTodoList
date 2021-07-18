package com.example.pam_project.database.utils

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pam_project.database.categories.CategoryDao
import com.example.pam_project.database.categories.CategoryEntity
import com.example.pam_project.database.lists.ListDao
import com.example.pam_project.database.lists.ListEntity
import com.example.pam_project.database.tasks.TaskDao
import com.example.pam_project.database.tasks.TaskEntity

@Database(entities = [ListEntity::class, CategoryEntity::class, TaskEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun listDao(): ListDao
    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        private const val DB_NAME = "db_pam"
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
                        .fallbackToDestructiveMigration().build()
            }
            return instance
        }
    }
}