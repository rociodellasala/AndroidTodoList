package com.example.pam_project.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "Tasks")
public class TaskEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    @NonNull
    public String name;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "priority")
    public boolean priority;

    @ColumnInfo(name = "status")
    @NonNull
    public String status;

    @NonNull
    public long listId;

    public TaskEntity(final long id, final String name, final String description, final boolean priority,
                      final String status, final long listId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.listId = listId;
        this.status = status;
    }

    @Ignore
    public TaskEntity(final String name, final String description, final boolean priority, final String status, final long listId) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.listId = listId;
        this.status = status;
    }

}