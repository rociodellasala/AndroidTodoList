package com.example.pam_project.lists.lists;

import com.example.pam_project.db.relationships.CategoriesWithLists;

import java.util.List;

import io.reactivex.Flowable;

public interface CategoriesRepository {

    Flowable<List<CategoriesWithLists>> getCategoriesWithLists();

}
