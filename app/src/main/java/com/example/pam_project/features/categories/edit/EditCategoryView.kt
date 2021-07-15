package com.example.pam_project.features.categories.edit

import com.example.pam_project.features.categories.list.CategoryInformation

interface EditCategoryView {
    fun bindCategory(model: CategoryInformation?)
    fun onCategoryRetrievedError()
    fun onCategoryDelete()
    fun onCategoryUpdateError()
    fun onCategoryDeletedError()
    fun showDeleteDialog()
}