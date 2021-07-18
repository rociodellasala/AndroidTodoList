package com.example.pam_project.database.categories

import com.example.pam_project.database.relationships.CategoriesWithLists
import com.example.pam_project.features.categories.list.CategoryInformation
import com.example.pam_project.features.lists.list.ListInformation
import com.example.pam_project.features.tasks.list.TaskInformation
import com.example.pam_project.utils.constants.TaskStatus
import java.util.*

class CategoryMapper {
    fun toModel(entity: CategoryEntity?): CategoryInformation {
        return CategoryInformation(entity!!.name, entity.color)
    }

    fun toListWithCategoriesModel(entities: List<CategoriesWithLists?>?): Map<CategoryInformation, List<ListInformation>> {
        val map: MutableMap<CategoryInformation, List<ListInformation>> = HashMap()
        for (categoryWithListEntity in entities!!) {
            val list: MutableList<ListInformation> = ArrayList()
            for (listWithTaskEntity in categoryWithListEntity!!.lists!!) {
                val listEntity = listWithTaskEntity.list
                val tasksEntity = listWithTaskEntity.tasks
                val task: MutableList<TaskInformation> = ArrayList()
                for (taskEntity in tasksEntity!!) {
                    task.add(TaskInformation(taskEntity.id, taskEntity.name, taskEntity.description,
                            taskEntity.priority, TaskStatus.getStatus(taskEntity.status)))
                }
                list.add(ListInformation(listEntity!!.id, listEntity.name, listEntity.categoryId,
                    categoryWithListEntity.category!!.color, task))
            }
            map[toModel(categoryWithListEntity.category)] = list
        }
        return map
    }

    fun toCategoryModel(categoryEntities: List<CategoryEntity?>?): List<CategoryInformation> {
        val list: MutableList<CategoryInformation> = ArrayList()
        for (entity in categoryEntities!!) {
            list.add(CategoryInformation(entity!!.id, entity.name, entity.color))
        }
        return list
    }
}