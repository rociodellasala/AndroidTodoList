package com.example.pam_project.features.categories.edit

import com.example.pam_project.features.categories.list.CategoryInformation
import com.example.pam_project.repositories.categories.CategoriesRepository
import com.example.pam_project.utils.schedulers.SchedulerProvider
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

class EditCategoryPresenter(private val categoryId: Long, private val provider: SchedulerProvider?,
                            private val repository: CategoriesRepository?, view: EditCategoryView?) {
    private val view: WeakReference<EditCategoryView?> = WeakReference(view)
    private var fetchCategoryDisposable: Disposable? = null
    private var updateCategoryDisposable: Disposable? = null
    private var deleteCategoryDisposable: Disposable? = null

    fun onViewAttached() {
        fetchCategoryDisposable = repository!!.getCategory(categoryId)
                .subscribeOn(provider!!.computation())
                .observeOn(provider.ui())
                .subscribe({ model: CategoryInformation? -> onCategoryRetrieved(model) }) { throwable: Throwable
                    -> onCategoryRetrievedError(throwable) }
    }

    private fun onCategoryRetrieved(model: CategoryInformation?) {
        if (view.get() != null) {
            view.get()!!.bindCategory(model)
        }
    }

    private fun onCategoryRetrievedError(throwable: Throwable) {
        if (view.get() != null) {
            view.get()!!.onCategoryRetrievedError()
        }
    }

    fun updateCategory(name: String, color: String) {
        updateCategoryDisposable = repository!!.updateCategory(categoryId, name, color)
                .subscribeOn(provider!!.computation())
                .observeOn(provider.ui())
                .subscribe({}) { throwable: Throwable -> onCategoryUpdateError(throwable) }
    }

    private fun onCategoryUpdateError(throwable: Throwable) {
        if (view.get() != null) {
            view.get()!!.onCategoryUpdateError()
        }
    }

    fun deleteCategory(id: Long) {
        deleteCategoryDisposable = repository!!.deleteCategory(id)
                .subscribeOn(provider!!.computation())
                .observeOn(provider.ui())
                .subscribe({ onCategoryDeleted() }) { throwable: Throwable -> onCategoryDeletedError(throwable) }
    }

    private fun onCategoryDeleted() {
        if (view.get() != null) {
            view.get()!!.onCategoryDelete()
        }
    }

    private fun onCategoryDeletedError(throwable: Throwable) {
        if (view.get() != null) {
            view.get()!!.onCategoryDeletedError()
        }
    }

    fun onDeletePressed() {
        if (view.get() != null) {
            view.get()!!.showDeleteDialog()
        }
    }

    fun onViewDetached() {
        if (fetchCategoryDisposable != null) fetchCategoryDisposable!!.dispose()
        if (updateCategoryDisposable != null) updateCategoryDisposable!!.dispose()
        if (deleteCategoryDisposable != null) deleteCategoryDisposable!!.dispose()
    }

}