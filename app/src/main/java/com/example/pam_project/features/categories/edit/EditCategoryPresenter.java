package com.example.pam_project.features.categories.edit;

import com.example.pam_project.features.categories.list.CategoryInformation;
import com.example.pam_project.repositories.categories.CategoriesRepository;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EditCategoryPresenter {
    private final long categoryId;
    private final CategoriesRepository repository;
    private final WeakReference<EditCategoryView> view;
    private Disposable updateCategoryDisposable;
    private Disposable deleteCategoryDisposable;

    public EditCategoryPresenter(final long categoryId, final CategoriesRepository repository,
                                 final EditCategoryView view) {
        this.categoryId = categoryId;
        this.repository = repository;
        this.view = new WeakReference<>(view);
    }

    public void onViewAttached() {
        if (view.get() != null) {
            CategoryInformation model = repository.getCategory(categoryId);
            view.get().bindCategory(model);
        }
    }

    public void updateCategory(final String name, final String color) {
        updateCategoryDisposable = repository.updateCategory(categoryId, name, color)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCategoryUpdate, this::onCategoryUpdateError);
    }

    private void onCategoryUpdate() {
       // TODO
    }

    private void onCategoryUpdateError(final Throwable throwable) {
        // TODO
    }

    public void deleteCategory(final long id) {
        deleteCategoryDisposable = repository.deleteCategory(id)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCategoryDeleted, this::onCategoryDeletedError);
    }

    private void onCategoryDeleted() {
        if (view.get() != null) {
            view.get().onCategoryDelete();
        }
    }

    private void onCategoryDeletedError(final Throwable throwable) {
        // TODO
    }

    public void onViewDetached() {
        if (updateCategoryDisposable != null)
            updateCategoryDisposable.dispose();
        if (deleteCategoryDisposable != null)
            deleteCategoryDisposable.dispose();
    }

}