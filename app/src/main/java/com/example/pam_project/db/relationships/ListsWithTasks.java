package com.example.pam_project.db.relationships;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.pam_project.db.entities.TaskEntity;
import com.example.pam_project.db.entities.ListEntity;

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
