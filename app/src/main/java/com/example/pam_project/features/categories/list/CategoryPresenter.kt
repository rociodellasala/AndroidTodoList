package com.example.pam_project.features.categories.list

import com.example.pam_project.repositories.categories.CategoriesRepository
import com.example.pam_project.utils.schedulers.SchedulerProvider
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

class CategoryPresenter(private val provider: SchedulerProvider?,
                        private val repository: CategoriesRepository?, view: CategoryView?) {
    private val view: WeakReference<CategoryView?> = WeakReference(view)
    private var fetchCategoriesDisposable: Disposable? = null
    fun onViewAttached() {
        if (view.get() != null) {
            fetchCategories()
        }
    }

    private fun fetchCategories() {
        fetchCategoriesDisposable = repository!!.categories
                .subscribeOn(provider!!.computation())
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

    fun onCategoryClicked(id: Long) {
        if (view.get() != null) view.get()!!.showCategoryForm(id)
    }

    fun onEmptyCategory() {
        if (view.get() != null) view.get()!!.showEmptyMessage()
    }

    fun onButtonAddClicked() {
        if (view.get() != null) view.get()!!.showAddCategory()
    }

    fun swapCategories(draggedPosition: Int, targetPosition: Int) {
        if (view.get() != null) view.get()!!.onCategoriesSwap(draggedPosition, targetPosition)
    }

    fun onViewDetached() {
        if (fetchCategoriesDisposable != null) fetchCategoriesDisposable!!.dispose()
    }

}