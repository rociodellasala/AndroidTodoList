package com.example.pam_project.database.relationships;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.pam_project.database.categories.CategoryEntity;
import com.example.pam_project.database.lists.ListEntity;

import java.util.List;

public class CategoriesWithLists {
    @Embedded
    public CategoryEntity category;
    @Relation(
            parentColumn = "id",
            entityColumn = "categoryId",
            entity = ListEntity.class
    )
    public List<ListsWithTasks> lists;
}