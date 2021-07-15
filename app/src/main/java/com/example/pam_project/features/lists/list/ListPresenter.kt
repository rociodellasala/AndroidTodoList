package com.example.pam_project.features.lists.list

import com.example.pam_project.features.categories.list.CategoryInformation
import com.example.pam_project.landing.FtuStorage
import com.example.pam_project.repositories.categories.CategoriesRepository
import com.example.pam_project.utils.schedulers.SchedulerProvider
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference
import java.util.*

class ListPresenter(private val ftuStorage: FtuStorage?, private val provider: SchedulerProvider?,
                    private val categoriesRepository: CategoriesRepository?,
                    view: ListView?) {
    private val view: WeakReference<ListView?>
    private var fetchListsDisposable: Disposable? = null
    private var fetchCategoriesDisposable: Disposable? = null
    fun onViewAttached() {
        if (ftuStorage!!.isActive) {
            ftuStorage.deactivate()
            if (view.get() != null) view.get()!!.launchFtu()
        } else {
            if (view.get() != null) {
                fetchCategories()
                fetchLists()
                view.get()!!.unFocusSearch()
            }
        }
    }

    private fun fetchCategories() {
        fetchCategoriesDisposable = categoriesRepository.getCategories()
                .subscribeOn(provider!!.computation())
                .observeOn(provider.ui())
                .subscribe({ model: List<CategoryInformation?>? -> onCategoriesReceived(model) }) { e: Throwable -> onCategoriesReceivedError(e) }
    }

    private fun onCategoriesReceived(model: List<CategoryInformation?>?) {
        if (view.get() != null) {
            val finalList: List<CategoryInformation?> = ArrayList(model)
            Collections.sort(finalList)
            view.get()!!.bindCategories(finalList)
        }
    }

    private fun onCategoriesReceivedError(e: Throwable) {
        if (view.get() != null) {
            view.get()!!.onCategoriesReceivedError()
        }
    }

    private fun fetchLists() {
        fetchListsDisposable = categoriesRepository.getCategoriesWithLists()
                .subscribeOn(provider!!.computation())
                .observeOn(provider.ui())
                .subscribe({ model: Map<CategoryInformation?, List<ListInformation?>?>? -> onListsReceived(model) }) { e: Throwable -> onListsReceivedError(e) }
    }

    private fun onListsReceived(model: Map<CategoryInformation?, List<ListInformation?>?>?) {
        if (view.get() != null) {
            val finalList: MutableList<ListInformation?> = ArrayList()
            for (list in model!!.values) {
                finalList.addAll(list!!)
            }
            Collections.sort(finalList)
            view.get()!!.bindLists(finalList)
        }
    }

    private fun onListsReceivedError(e: Throwable) {
        if (view.get() != null) {
            view.get()!!.onListsReceivedError()
        }
    }

    fun performSearch(searchQuery: String?) {
        if (view.get() != null) view.get()!!.bindSearchedLists(searchQuery)
    }

    fun onSearchDetached() {
        if (view.get() != null) view.get()!!.unbindSearchedLists()
    }

    fun onListClicked(id: Long) {
        if (view.get() != null) view.get()!!.showListContent(id)
    }

    fun onButtonClicked() {
        if (view.get() != null) view.get()!!.showAddList()
    }

    fun onFilterDialog() {
        if (view.get() != null) {
            view.get()!!.showFilterDialog()
            view.get()!!.unFocusSearch()
        }
    }

    fun onSortByDialog() {
        if (view.get() != null) {
            view.get()!!.showSortByDialog()
            view.get()!!.unFocusSearch()
        }
    }

    fun onEmptyList() {
        if (view.get() != null) view.get()!!.showEmptyMessage()
    }

    fun onManageCategories() {
        if (view.get() != null) {
            view.get()!!.showManageCategories()
            view.get()!!.unFocusSearch()
        }
    }

    fun onAboutSection() {
        if (view.get() != null) view.get()!!.showAboutSection()
    }

    fun onViewDetached() {
        if (fetchListsDisposable != null) fetchListsDisposable!!.dispose()
        if (fetchCategoriesDisposable != null) fetchCategoriesDisposable!!.dispose()
    }

    init {
        this.view = WeakReference(view)
    }
}