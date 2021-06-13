package com.example.pam_project.database.utils;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.pam_project.database.categories.CategoryDao;
import com.example.pam_project.database.categories.CategoryEntity;
import com.example.pam_project.database.lists.ListDao;
import com.example.pam_project.database.lists.ListEntity;
import com.example.pam_project.database.tasks.TaskDao;
import com.example.pam_project.database.tasks.TaskEntity;

@Database(entities = {ListEntity.class, CategoryEntity.class, TaskEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "db_pam";
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration().build();
        }

        return instance;
    }

    public abstract ListDao listDao();

    public abstract TaskDao taskDao();

    public abstract CategoryDao categoryDao();
}