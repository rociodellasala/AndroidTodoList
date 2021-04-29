package com.example.pam_project.lists.lists;

import com.example.pam_project.db.entities.ListEntity;

import java.util.List;

import io.reactivex.Flowable;

public interface ListsRepository {

    Flowable<List<ListEntity>> getLists();
}
