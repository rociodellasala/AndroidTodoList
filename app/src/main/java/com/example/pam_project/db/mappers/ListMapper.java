package com.example.pam_project.db.mappers;

import com.example.pam_project.db.entities.ListEntity;
import com.example.pam_project.lists.lists.components.ListInformation;
import com.example.pam_project.utils.AppColor;

import java.util.Arrays;
import java.util.List;

public class ListMapper {

    public ListInformation toModel(final ListEntity entity){
        return new ListInformation(entity.id, entity.name, entity.categoryId);
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
