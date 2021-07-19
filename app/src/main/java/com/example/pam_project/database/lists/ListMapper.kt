package com.example.pam_project.database.lists

import com.example.pam_project.database.relationships.ListsWithTasks
import com.example.pam_project.features.lists.list.ListInformation
import com.example.pam_project.features.tasks.list.TaskInformation
import com.example.pam_project.utils.constants.TaskStatus
import java.util.*

class ListMapper {
    fun toModel(entity: ListEntity?): ListInformation {
        return ListInformation(entity!!.id, entity.name, entity.categoryId)
    }

    private fun toModelWithTasks(entity: ListEntity?, tasks: List<TaskInformation>?): ListInformation {
        return ListInformation(entity!!.id, entity.name, entity.categoryId, tasks)
    }

    fun toListWithTasksModel(entity: ListsWithTasks?): ListInformation {
        val listOfTasks: MutableList<TaskInformation> = ArrayList()
        for (taskEntity in entity!!.tasks!!) {
            listOfTasks.add(TaskInformation(
                id = taskEntity.id,
                title = taskEntity.name,
                description = taskEntity.description,
                isUrgent = taskEntity.priority,
                status = TaskStatus.getStatus(taskEntity.status)
            ))
        }
        return toModelWithTasks(entity.list, listOfTasks)
    }
}