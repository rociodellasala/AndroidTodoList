package com.example.pam_project.features.categories.edit;

import com.example.pam_project.features.categories.list.CategoryInformation;
import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.Disposable;

public class EditCategoryPresenter {
    private final long categoryId;
    private final SchedulerProvider provider;
    private final CategoriesRepository repository;
    private final WeakReference<EditCategoryView> view;
    private Disposable updateCategoryDisposable;
    private Disposable deleteCategoryDisposable;

    public EditCategoryPresenter(final long categoryId, final SchedulerProvider provider,
                                 final CategoriesRepository repository,
                                 final EditCategoryView view) {
        this.categoryId = categoryId;
        this.provider = provider;
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
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .doOnError(this::onCategoryUpdateError)
                .subscribe();
    }

    private void onCategoryUpdateError(final Throwable throwable) {
        if (view.get() != null) {
            view.get().onCategoryUpdateError();
        }
    }

    public void deleteCategory(final long id) {
        deleteCategoryDisposable = repository.deleteCategory(id)
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .subscribe(this::onCategoryDeleted, this::onCategoryDeletedError);
    }

    private void onCategoryDeleted() {
        if (view.get() != null) {
            view.get().onCategoryDelete();
        }
    }

    private void onCategoryDeletedError(final Throwable throwable) {
        if (view.get() != null) {
            view.get().onCategoryDeletedError();
        }
    }

    public void onViewDetached() {
        if (updateCategoryDisposable != null)
            updateCategoryDisposable.dispose();
        if (deleteCategoryDisposable != null)
            deleteCategoryDisposable.dispose();
    }

}