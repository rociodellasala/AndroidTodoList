package com.example.pam_project.database.tasks

import androidx.room.*
import com.example.pam_project.database.lists.ListEntity

@Entity(tableName = "Tasks")
class TaskEntity {
    @kotlin.jvm.JvmField
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @kotlin.jvm.JvmField
    @ColumnInfo(name = "name")
    val name: String?

    @kotlin.jvm.JvmField
    @ColumnInfo(name = "description")
    val description: String?

    @kotlin.jvm.JvmField
    @ColumnInfo(name = "priority")
    val priority: Boolean

    @kotlin.jvm.JvmField
    @ColumnInfo(name = "status")
    val status: String?

    @kotlin.jvm.JvmField
    @ForeignKey(entity = ListEntity::class, parentColumns = ["id"], childColumns = ["listId"], onDelete = ForeignKey.CASCADE)
    val listId: Long

    constructor(id: Long, name: String?, description: String?, priority: Boolean,
                status: String?, listId: Long) {
        this.id = id
        this.name = name
        this.description = description
        this.priority = priority
        this.listId = listId
        this.status = status
    }

    @Ignore
    constructor(name: String?, description: String?, priority: Boolean, status: String?, listId: Long) {
        this.name = name
        this.description = description
        this.priority = priority
        this.listId = listId
        this.status = status
    }
}