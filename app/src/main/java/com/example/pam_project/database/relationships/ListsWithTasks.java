package com.example.pam_project.database.relationships;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.pam_project.database.lists.ListEntity;
import com.example.pam_project.database.tasks.TaskEntity;

import java.util.List;

public class ListsWithTasks {
    @Embedded
    public ListEntity list;
    @Relation(
            parentColumn = "id",
            entityColumn = "listId"
    )
    public List<TaskEntity> tasks;
}