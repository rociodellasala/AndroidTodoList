package com.example.pam_project.features.lists.create

import com.example.pam_project.features.categories.list.CategoryInformation

interface CreateListView {
    fun bindCategories(model: List<CategoryInformation?>?)
    fun onListInsertedError()
    fun onCategoriesReceivedError()
}