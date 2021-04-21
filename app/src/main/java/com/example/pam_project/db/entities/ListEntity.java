package com.example.pam_project.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Lists")
public class ListEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    public long categoryId;

    public ListEntity(long id, String name, long categoryId) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
    }

    @Ignore
    public ListEntity(String name, long categoryId) {
        this.name = name;
        this.categoryId = categoryId;
    }
}