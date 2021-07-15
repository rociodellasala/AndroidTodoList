package com.example.pam_project.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.pam_project.database.lists.ListEntity
import com.example.pam_project.database.tasks.TaskEntity

class ListsWithTasks {
    @kotlin.jvm.JvmField
    @Embedded
    var list: ListEntity? = null

    @kotlin.jvm.JvmField
    @Relation(parentColumn = "id", entityColumn = "listId")
    var tasks: List<TaskEntity>? = null
}