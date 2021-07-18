package com.example.pam_project.features.lists.create

import com.example.pam_project.features.categories.list.CategoryInformation
import com.example.pam_project.repositories.categories.CategoriesRepository
import com.example.pam_project.repositories.lists.ListsRepository
import com.example.pam_project.utils.schedulers.SchedulerProvider
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

class CreateListPresenter(private val provider: SchedulerProvider,
                          private val categoriesRepository: CategoriesRepository,
                          private val listsRepository: ListsRepository, view: CreateListView) {
    private val view: WeakReference<CreateListView> = WeakReference(view)
    private var fetchListsDisposable: Disposable? = null
    private var insertListDisposable: Disposable? = null

    fun onViewAttached() {
        if (view.get() != null) fetchCategories()
    }

    private fun fetchCategories() {
        fetchListsDisposable = categoriesRepository.categories
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .subscribe({ model: List<CategoryInformation?>? -> onCategoriesReceived(model) })
                { throwable: Throwable -> onCategoriesReceivedError(throwable) }
    }

    private fun onCategoriesReceived(model: List<CategoryInformation?>?) {
        if (view.get() != null) {
            view.get()!!.bindCategories(model)
        }
    }

    private fun onCategoriesReceivedError(throwable: Throwable) {
        if (view.get() != null) {
            view.get()!!.onCategoriesReceivedError()
        }
    }

    fun insertList(name: String, categoryId: Long?) {
        insertListDisposable = listsRepository.insertList(name, categoryId!!)
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .subscribe({}) { throwable: Throwable -> onListInsertedError(throwable) }
    }

    private fun onListInsertedError(throwable: Throwable) {
        if (view.get() != null) {
            view.get()!!.onListInsertedError()
        }
    }

    fun onViewDetached() {
        if (fetchListsDisposable != null) fetchListsDisposable!!.dispose()
        if (insertListDisposable != null) insertListDisposable!!.dispose()
    }

}