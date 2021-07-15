package com.example.pam_project.database.categories

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "Categories")
class CategoryEntity {
    @kotlin.jvm.JvmField
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @kotlin.jvm.JvmField
    @ColumnInfo(name = "name")
    val name: String

    @kotlin.jvm.JvmField
    @ColumnInfo(name = "color")
    val color: String

    constructor(id: Long, name: String, color: String) {
        this.id = id
        this.name = name
        this.color = color
    }

    @Ignore
    constructor(name: String, color: String) {
        this.name = name
        this.color = color
    }
}