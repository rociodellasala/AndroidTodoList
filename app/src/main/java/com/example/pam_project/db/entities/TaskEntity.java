package com.example.pam_project.db.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Tasks")
public class TaskEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;

    public String description;

    public boolean priority;

    public String status;

    public long listId;

    public TaskEntity(long id, String name, String description, boolean priority, String status, long listId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.listId = listId;
        this.status = status;
    }

    @Ignore
    public TaskEntity(String name, String description, boolean priority, String status, long listId) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.listId = listId;
        this.status = status;
    }

}