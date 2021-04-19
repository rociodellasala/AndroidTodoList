package com.example.pam_project.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Categories")
public class CategoryEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "color")
    public String color;

    public CategoryEntity(int id, String name, String color) {
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