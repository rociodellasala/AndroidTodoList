package com.example.pam_project.db.mappers;

import com.example.pam_project.db.entities.CategoryEntity;
import com.example.pam_project.db.entities.ListEntity;
import com.example.pam_project.db.relationships.CategoriesWithLists;
import com.example.pam_project.lists.categories.CategoryInformation;
import com.example.pam_project.lists.lists.components.ListInformation;
import com.example.pam_project.utils.AppColor;
import com.example.pam_project.utils.ColorFinder;

import java.util.ArrayList;
import java.util.List;


public class CategoryMapper {

    public CategoryInformation toModel(final CategoryEntity entity){
        return new CategoryInformation(entity.name, ColorFinder.findColor(entity.color));
    }

    public List<ListInformation> toListModel(final List<CategoriesWithLists> entities){
        final List<ListInformation> list = new ArrayList<>();

        for (final CategoriesWithLists entity : entities) {
            for (final ListEntity listEntity : entity.lists) {
                AppColor color = ColorFinder.findColor(entity.category.color);
                list.add(new ListInformation(listEntity.id, listEntity.name, listEntity.categoryId, color));
            }
        }

        return list;
    }

    public List<CategoryInformation> toCategoryModel(final List<CategoryEntity> categoryEntities) {
        final List<CategoryInformation> list = new ArrayList<>();

        for(CategoryEntity entity: categoryEntities){
            AppColor color = ColorFinder.findColor(entity.color);
            list.add(new CategoryInformation(entity.id, entity.name, color));
        }

        return list;
    }

}
