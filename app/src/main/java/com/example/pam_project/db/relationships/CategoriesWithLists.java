package com.example.pam_project.db.relationships;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.pam_project.db.entities.CategoryEntity;
import com.example.pam_project.db.entities.ListEntity;

import java.util.List;

public class CategoriesWithLists {
    @Embedded public CategoryEntity category;
    @Relation(
            parentColumn = "id",
            entityColumn = "categoryId"
    )
    public List<ListEntity> lists;
}