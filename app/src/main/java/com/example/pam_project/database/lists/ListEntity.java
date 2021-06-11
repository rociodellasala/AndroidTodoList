package com.example.pam_project.database.lists;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.pam_project.database.categories.CategoryEntity;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "Lists", foreignKeys = @ForeignKey(entity = CategoryEntity.class,
        parentColumns = "id",
        childColumns = "categoryId",
        onDelete = CASCADE))
public class ListEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    public final String name;

    @ColumnInfo(name = "categoryId")
    public final long categoryId;

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