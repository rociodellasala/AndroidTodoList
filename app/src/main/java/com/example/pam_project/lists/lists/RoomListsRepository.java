package com.example.pam_project.lists.lists;

import com.example.pam_project.db.daos.ListDao;
import com.example.pam_project.db.entities.ListEntity;
import com.example.pam_project.db.mappers.ListMapper;

import java.util.List;

import io.reactivex.Flowable;

public class RoomListsRepository implements ListsRepository{

    private final ListDao listDao;

    public RoomListsRepository(final ListDao listDao){
        this.listDao = listDao;
    }

    @Override
    public Flowable<List<ListEntity>> getLists() {
        return listDao.getAllLists();
    }
}
