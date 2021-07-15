package com.example.pam_project.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.pam_project.database.categories.CategoryEntity
import com.example.pam_project.database.lists.ListEntity

class CategoriesWithLists {
    @kotlin.jvm.JvmField
    @Embedded
    var category: CategoryEntity? = null

    @kotlin.jvm.JvmField
    @Relation(parentColumn = "id", entityColumn = "categoryId", entity = ListEntity::class)
    var lists: List<ListsWithTasks>? = null
}