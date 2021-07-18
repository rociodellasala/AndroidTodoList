package com.example.pam_project.features.lists.edit

import com.example.pam_project.features.categories.list.CategoryInformation
import com.example.pam_project.features.lists.list.ListInformation
import com.example.pam_project.repositories.categories.CategoriesRepository
import com.example.pam_project.repositories.lists.ListsRepository
import com.example.pam_project.utils.schedulers.SchedulerProvider
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

class EditListPresenter(private val provider: SchedulerProvider,
                        private val categoriesRepository: CategoriesRepository,
                        private val listsRepository: ListsRepository,
                        view: EditListView) {
    private val view: WeakReference<EditListView?> = WeakReference(view)
    private var fetchListDisposable: Disposable? = null
    private var fetchCategoriesDisposable: Disposable? = null
    private var updateListDisposable: Disposable? = null
    private var deleteListDisposable: Disposable? = null
    fun onViewAttached(id: Long) {
        if (view.get() != null) {
            fetchCategories(id)
        }
    }

    private fun fetchCategories(id: Long) {
        fetchCategoriesDisposable = categoriesRepository.categories
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .subscribe({ model: List<CategoryInformation?>? ->
                    if (view.get() != null) {
                        view.get()!!.bindCategories(model)
                        fetchList(id)
                    }
                }) { throwable: Throwable -> onCategoriesReceivedError(throwable) }
    }

    private fun fetchList(id: Long) {
        fetchListDisposable = listsRepository.getList(id)
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .subscribe({ model: ListInformation? -> onListReceived(model) }) { throwable: Throwable -> onListReceivedError(throwable) }
    }

    private fun onListReceived(model: ListInformation?) {
        if (view.get() != null) view.get()!!.bindList(model)
    }

    private fun onListReceivedError(throwable: Throwable) {
        if (view.get() != null) {
            view.get()!!.onListReceivedError()
        }
    }

    private fun onCategoriesReceivedError(throwable: Throwable) {
        if (view.get() != null) {
            view.get()!!.onCategoriesReceivedError()
        }
    }

    fun updateList(id: Long, name: String, categoryId: Long?) {
        updateListDisposable = listsRepository.updateList(id, name, categoryId!!)
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .subscribe({}) { throwable: Throwable -> onListUpdatedError(throwable) }
    }

    private fun onListUpdatedError(throwable: Throwable) {
        if (view.get() != null) {
            view.get()!!.onListUpdatedError()
        }
    }

    fun deleteList(id: Long) {
        deleteListDisposable = listsRepository.deleteList(id)
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .subscribe({ onListDeleted() }) { throwable: Throwable -> onListDeletedError(throwable) }
    }

    private fun onListDeleted() {
        if (view.get() != null) {
            view.get()!!.onListDelete()
        }
    }

    private fun onListDeletedError(throwable: Throwable) {
        if (view.get() != null) {
            view.get()!!.onListDeletedError()
        }
    }

    fun onDeletePressed() {
        if (view.get() != null) {
            view.get()!!.showDeleteDialog()
        }
    }

    fun onViewDetached() {
        if (fetchListDisposable != null) fetchListDisposable!!.dispose()
        if (fetchCategoriesDisposable != null) fetchCategoriesDisposable!!.dispose()
        if (updateListDisposable != null) updateListDisposable!!.dispose()
        if (deleteListDisposable != null) deleteListDisposable!!.dispose()
    }

}