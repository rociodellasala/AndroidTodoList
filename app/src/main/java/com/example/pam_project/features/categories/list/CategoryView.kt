package com.example.pam_project.features.categories.list

interface CategoryView {
    fun bindCategories(model: List<CategoryInformation?>?)
    fun showAddCategory()
    fun showCategoryForm(id: Long)
    fun showEmptyMessage()
    fun onCategoriesSwap(draggedPosition: Int, targetPosition: Int)
    fun onCategoriesReceivedError()
}