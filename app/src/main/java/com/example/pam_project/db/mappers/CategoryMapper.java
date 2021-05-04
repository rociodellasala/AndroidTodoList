package com.example.pam_project.db.mappers;

import android.util.Log;

import com.example.pam_project.db.entities.CategoryEntity;
import com.example.pam_project.db.entities.ListEntity;
import com.example.pam_project.db.entities.TaskEntity;
import com.example.pam_project.db.relationships.CategoriesWithLists;
import com.example.pam_project.db.relationships.ListsWithTasks;
import com.example.pam_project.lists.categories.components.CategoryInformation;
import com.example.pam_project.lists.lists.components.ListInformation;
import com.example.pam_project.lists.tasks.components.TaskInformation;
import com.example.pam_project.utils.AppColor;
import com.example.pam_project.utils.ColorFinder;
import com.example.pam_project.utils.StatusMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryMapper {

    public CategoryInformation toModel(final CategoryEntity entity) {
        return new CategoryInformation(entity.name, ColorFinder.findColor(entity.color));
    }

    public Map<CategoryInformation, List<ListInformation>> toListWithCategoriesModel(final List<CategoriesWithLists> entities) {
        final Map<CategoryInformation, List<ListInformation>> map = new HashMap<>();

        for (final CategoriesWithLists categoryWithListEntity : entities) {
            final List<ListInformation> list = new ArrayList<>();
            for (final ListsWithTasks listWithTaskEntity : categoryWithListEntity.lists) {
                ListEntity listEntity = listWithTaskEntity.list;
                List<TaskEntity> tasksEntity = listWithTaskEntity.tasks;
                List<TaskInformation> task = new ArrayList<>();
                for (final TaskEntity taskEntity : tasksEntity) {
                    task.add(new TaskInformation(taskEntity.id, taskEntity.name, taskEntity.description,
                            taskEntity.priority, StatusMapper.toTaskStatusMapper(taskEntity.status)));
                }
                AppColor color = ColorFinder.findColor(categoryWithListEntity.category.color);
                list.add(new ListInformation(listEntity.id, listEntity.name, listEntity.categoryId, color, task));
            }

            map.put(toModel(categoryWithListEntity.category), list);
        }

        return map;
    }

    public List<CategoryInformation> toCategoryModel(final List<CategoryEntity> categoryEntities) {
        final List<CategoryInformation> list = new ArrayList<>();

        for (CategoryEntity entity : categoryEntities) {
            AppColor color = ColorFinder.findColor(entity.color);
            list.add(new CategoryInformation(entity.id, entity.name, color));
        }

        return list;
    }

}
