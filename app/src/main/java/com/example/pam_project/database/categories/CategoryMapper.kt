package com.example.pam_project.database.categories

import com.example.pam_project.database.relationships.CategoriesWithLists
import com.example.pam_project.features.categories.list.CategoryInformation
import com.example.pam_project.features.lists.list.ListInformation
import com.example.pam_project.features.tasks.list.TaskInformation
import com.example.pam_project.utils.constants.TaskStatus
import java.util.*

class CategoryMapper {
    fun toModel(entity: CategoryEntity?): CategoryInformation {
        return CategoryInformation(
            title = entity!!.name,
            color = entity.color
        )
    }

    fun toListWithCategoriesModel(entities: List<CategoriesWithLists?>?): Map<CategoryInformation, List<ListInformation>> {
        val map: MutableMap<CategoryInformation, List<ListInformation>> = HashMap()
        for (categoryWithListEntity in entities!!) {
            val list: MutableList<ListInformation> = ArrayList()
            for (listWithTaskEntity in categoryWithListEntity!!.lists) {
                val listEntity = listWithTaskEntity.list
                val tasksEntity = listWithTaskEntity.tasks
                val task: MutableList<TaskInformation> = ArrayList()
                for (taskEntity in tasksEntity) {
                    task.add(
                        TaskInformation(
                            id = taskEntity.id,
                            title = taskEntity.name,
                            description = taskEntity.description,
                            isUrgent = taskEntity.priority,
                            status = TaskStatus.getStatus(taskEntity.status)
                        ))
                }
                list.add(ListInformation(
                    id = listEntity.id, title = listEntity.name, categoryId = listEntity.categoryId,
                    color = categoryWithListEntity.category.color, tasks = task))
            }
            map[toModel(categoryWithListEntity.category)] = list
        }
        return map
    }

    fun toCategoryModel(categoryEntities: List<CategoryEntity?>?): List<CategoryInformation> {
        val list: MutableList<CategoryInformation> = ArrayList()
        for (entity in categoryEntities!!) {
            list.add(CategoryInformation(
                id = entity!!.id,
                title = entity.name,
                color = entity.color
            ))
        }
        return list
    }
}