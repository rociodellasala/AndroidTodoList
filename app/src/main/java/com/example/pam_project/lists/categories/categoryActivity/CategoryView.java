package com.example.pam_project.lists.categories.categoryActivity;

import com.example.pam_project.lists.categories.components.CategoryInformation;

import java.util.List;

public interface CategoryView {

    void bindCategories(final List<CategoryInformation> model);

    void showCategories();

    void showCategoryForm(final long id);
}
