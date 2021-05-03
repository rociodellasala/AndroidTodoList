package com.example.pam_project.db.repositories;

import com.example.pam_project.lists.categories.CategoryInformation;
import com.example.pam_project.lists.lists.components.ListInformation;

import java.util.List;

import io.reactivex.Flowable;

public interface CategoriesRepository {

    Flowable<CategoryInformation> getCategory(final long id);

    Flowable<List<ListInformation>> getCategoriesWithLists();

    Flowable<List<CategoryInformation>> getCategories();
}
