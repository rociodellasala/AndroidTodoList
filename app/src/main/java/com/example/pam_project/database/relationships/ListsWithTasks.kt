package com.example.pam_project.database.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.example.pam_project.database.lists.ListEntity
import com.example.pam_project.database.tasks.TaskEntity

data class ListsWithTasks (@Embedded var list: ListEntity,
                           @Relation(parentColumn = "id", entityColumn = "listId") var tasks: List<TaskEntity>)