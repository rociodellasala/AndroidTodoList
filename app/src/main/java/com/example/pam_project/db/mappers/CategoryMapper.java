package com.example.pam_project.db.mappers;

import com.example.pam_project.db.entities.CategoryEntity;
import com.example.pam_project.db.entities.ListEntity;
import com.example.pam_project.db.relationships.CategoriesWithLists;
import com.example.pam_project.lists.categories.CategoryInformation;
import com.example.pam_project.lists.lists.components.ListInformation;
import com.example.pam_project.utils.AppColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CategoryMapper {

    public CategoryInformation toModel(final CategoryEntity entity){
        return new CategoryInformation(entity.name, findColor(entity.color));
    }

    public List<ListInformation> toModel(final List<CategoriesWithLists> entities){
        final List<ListInformation> list = new ArrayList<>();

        for (final CategoriesWithLists entity : entities) {
            for (final ListEntity listEntity : entity.lists) {
                AppColor color = findColor(entity.category.color);
                list.add(new ListInformation(listEntity.id, listEntity.name, listEntity.categoryId, color));
            }
        }

        return list;
    }


    private static AppColor findColor(String color) {
        final List<AppColor> colors = Arrays.asList(AppColor.values());

        for (int i = 0; i < colors.size(); i++) {
            if (color.equals(colors.get(i).toString())) {
                return colors.get(i);
            }
        }

        return null;
    }
}
