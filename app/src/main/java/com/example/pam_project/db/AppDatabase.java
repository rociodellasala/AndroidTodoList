package com.example.pam_project.db;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.pam_project.db.daos.ListDao;
import com.example.pam_project.db.daos.TaskDao;
import com.example.pam_project.db.daos.CategoryDao;
import com.example.pam_project.db.entities.CategoryEntity;
import com.example.pam_project.db.entities.ListEntity;
import com.example.pam_project.db.entities.TaskEntity;

@Database(entities = {ListEntity.class, CategoryEntity.class, TaskEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "db_pam";
    private static AppDatabase instance;

    public abstract ListDao listDao();
    public abstract TaskDao taskDao();
    public abstract CategoryDao categoryDao();

    public static synchronized AppDatabase getInstance(final Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration().build();
        }

        return instance;
    }

    public static synchronized void nukeDatabase() {
        if(instance != null) {
            Log.d("Database", "Clear all tables");
            instance.clearAllTables();
        }
    }
}
