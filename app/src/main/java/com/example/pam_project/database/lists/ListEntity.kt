package com.example.pam_project.database.lists

import androidx.room.*
import com.example.pam_project.database.categories.CategoryEntity

@Entity(tableName = "Lists")
class ListEntity {
    @kotlin.jvm.JvmField
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @kotlin.jvm.JvmField
    @ColumnInfo(name = "name")
    val name: String

    @kotlin.jvm.JvmField
    @ForeignKey(entity = CategoryEntity::class, parentColumns = ["id"], childColumns = ["categoryId"], onDelete = ForeignKey.CASCADE)
    val categoryId: Long

    constructor(id: Long, name: String, categoryId: Long) {
        this.id = id
        this.name = name
        this.categoryId = categoryId
    }

    @Ignore
    constructor(name: String, categoryId: Long) {
        this.name = name
        this.categoryId = categoryId
    }
}