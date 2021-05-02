package com.example.pam_project.db.mappers;

import com.example.pam_project.db.entities.ListEntity;
import com.example.pam_project.lists.lists.components.ListInformation;

public class ListMapper {

    public ListInformation toModel(final ListEntity entity){
        return new ListInformation(entity.id, entity.name, entity.categoryId);
    }

}
