package com.example.pam_project.features.categories.create

import com.example.pam_project.repositories.categories.CategoriesRepository
import com.example.pam_project.utils.schedulers.SchedulerProvider
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

class CreateCategoryPresenter(private val provider: SchedulerProvider?, private val repository: CategoriesRepository?, view: CreateCategoryView?) {
    private val view: WeakReference<CreateCategoryView?>
    private var insertCategoryDisposable: Disposable? = null
    fun insertCategory(name: String, color: String) {
        insertCategoryDisposable = repository!!.insertCategory(name, color)
                .subscribeOn(provider!!.computation())
                .observeOn(provider.ui())
                .subscribe({}) { throwable: Throwable -> onCategoryInsertedError(throwable) }
    }

    private fun onCategoryInsertedError(throwable: Throwable) {
        if (view.get() != null) {
            view.get()!!.onCategoryInsertedError()
        }
    }

    fun onViewDetached() {
        if (insertCategoryDisposable != null) insertCategoryDisposable!!.dispose()
    }

    init {
        this.view = WeakReference(view)
    }
}