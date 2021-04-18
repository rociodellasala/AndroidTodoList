package com.example.pam_project.db.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Tasks")
public class TaskEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    public String description;

    public boolean priority;

    public String status;

    public int listId;

    public TaskEntity(int id, String name, String description, boolean priority, String status, int listId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.listId = listId;
        this.status = status;
    }

    @Ignore
    public TaskEntity(String name, String description, boolean priority, String status, int listId) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.listId = listId;
        this.status = status;
    }

}
