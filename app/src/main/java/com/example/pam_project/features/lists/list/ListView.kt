package com.example.pam_project.features.lists.list

import com.example.pam_project.features.categories.list.CategoryInformation

interface ListView {
    fun launchFtu()
    fun unFocusSearch()
    fun bindCategories(model: List<CategoryInformation?>?)
    fun bindLists(model: List<ListInformation?>?)
    fun showAddList()
    fun bindSearchedLists(searchQuery: String?)
    fun unbindSearchedLists()
    fun showFilterDialog()
    fun showSortByDialog()
    fun showManageCategories()
    fun showListContent(id: Long)
    fun showEmptyMessage()
    fun onListsReceivedError()
    fun onCategoriesReceivedError()
    fun showAboutSection()
}