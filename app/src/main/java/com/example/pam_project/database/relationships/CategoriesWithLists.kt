package com.example.pam_project.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.pam_project.database.categories.CategoryEntity
import com.example.pam_project.database.lists.ListEntity

data class CategoriesWithLists(@Embedded var category: CategoryEntity,
                               @Relation(parentColumn = "id", entityColumn = "categoryId", entity = ListEntity::class)
                               var lists: List<ListsWithTasks>)