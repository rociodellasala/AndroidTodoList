package com.example.pam_project.features.categories.list;

import com.example.pam_project.repositories.categories.CategoriesRepository;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CategoryPresenter {

    private final CategoriesRepository repository;
    private final WeakReference<CategoryView> view;
    private Disposable fetchCategoriesDisposable;

    public CategoryPresenter(final CategoriesRepository repository, final CategoryView view) {
        this.repository = repository;
        this.view = new WeakReference<>(view);
    }

    public void onViewAttached() {
        if (view.get() != null) {
            view.get().showCategories();
            fetchCategories();
        }
    }

    public void onViewDetached() {
        if (fetchCategoriesDisposable != null)
            fetchCategoriesDisposable.dispose();
    }

    private void fetchCategories() {
        fetchCategoriesDisposable = repository.getCategories()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if (view.get() != null) {
                        view.get().bindCategories(model);
                    }
                });
    }

    public void onCategoryClicked(final long id) {
        if (view.get() != null)
            view.get().showCategoryForm(id);
    }

    public void onEmptyCategory(){
        if (view.get() != null)
            view.get().showEmptyMessage();
    }

    public void onButtonAddClicked() {
        if (view.get() != null)
            view.get().showAddCategory();
    }

    public void swapCategories(final int draggedPosition, final int targetPosition){
        if (view.get() != null)
            view.get().onCategoriesSwap(draggedPosition, targetPosition);
    }
}
