package com.example.pam_project.repositories.categories;

import com.example.pam_project.features.categories.list.CategoryInformation;
import com.example.pam_project.features.lists.list.ListInformation;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

public interface CategoriesRepository {

    CategoryInformation getCategory(final long id);

    Flowable<Map<CategoryInformation, List<ListInformation>>> getCategoriesWithLists();

    Flowable<List<CategoryInformation>> getCategories();

    long insertCategory(String name, String color);

    void updateCategory(long id, String name, String color);
}
