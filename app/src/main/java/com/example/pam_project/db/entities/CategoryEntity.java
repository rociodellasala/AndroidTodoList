package com.example.pam_project.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Categories")
public class CategoryEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    public final String name;

    @ColumnInfo(name = "color")
    public final String color;

    public CategoryEntity(long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    @Ignore
    public CategoryEntity(String name, String color) {
        this.name = name;
        this.color = color;
    }
}