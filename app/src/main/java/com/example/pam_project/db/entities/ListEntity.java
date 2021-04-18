package com.example.pam_project.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Lists")
public class ListEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    public int categoryId;

    public ListEntity(int id, String name, int categoryId) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
    }

    @Ignore
    public ListEntity(String name, int categoryId) {
        this.name = name;
        this.categoryId = categoryId;
    }
}
