package com.example.pam_project.database.tasks;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.pam_project.database.lists.ListEntity;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "Tasks",foreignKeys = @ForeignKey(entity = ListEntity.class,
        parentColumns = "id",
        childColumns = "listId",
        onDelete = CASCADE))
public class TaskEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    public final String name;

    @ColumnInfo(name = "description")
    public final String description;

    @ColumnInfo(name = "priority")
    public final boolean priority;

    @ColumnInfo(name = "status")
    public final String status;


    public final long listId;

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