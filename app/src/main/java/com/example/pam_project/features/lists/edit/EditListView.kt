package com.example.pam_project.features.lists.edit

import com.example.pam_project.features.categories.list.CategoryInformation
import com.example.pam_project.features.lists.list.ListInformation

interface EditListView {
    fun bindCategories(model: List<CategoryInformation?>?)
    fun bindList(model: ListInformation?)
    fun onListDelete()
    fun onListDeletedError()
    fun onListUpdatedError()
    fun onCategoriesReceivedError()
    fun showDeleteDialog()
    fun onListReceivedError()
}