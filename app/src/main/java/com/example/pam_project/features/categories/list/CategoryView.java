package com.example.pam_project.features.categories.list;

import java.util.List;

public interface CategoryView {

    void bindCategories(final List<CategoryInformation> model);

    void showAddCategory();

    void showCategoryForm(final long id);

    void showEmptyMessage();

    void onCategoriesSwap(final int draggedPosition, final int targetPosition);

    void onCategoriesReceivedError();
}
