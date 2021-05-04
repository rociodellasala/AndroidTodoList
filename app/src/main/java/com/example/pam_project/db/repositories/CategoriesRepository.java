package com.example.pam_project.db.repositories;

import com.example.pam_project.lists.categories.components.CategoryInformation;
import com.example.pam_project.lists.lists.components.ListInformation;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

public interface CategoriesRepository {

    Flowable<CategoryInformation> getCategory(final long id);

    Flowable<Map<CategoryInformation, List<ListInformation>>> getCategoriesWithLists();

    Flowable<List<CategoryInformation>> getCategories();
}
