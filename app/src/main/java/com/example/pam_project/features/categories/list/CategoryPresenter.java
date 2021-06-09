package com.example.pam_project.features.categories.list;

import com.example.pam_project.repositories.categories.CategoriesRepository;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CategoryPresenter {

    private final CategoriesRepository repository;
    private final WeakReference<CategoryView> view;
    private Disposable disposable;

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
        if (disposable != null)
            disposable.dispose();
    }

    private void fetchCategories() {
        disposable = repository.getCategories()
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

    public void swapCategories(final int draggedPosition, final int targetPosition){
        if (view.get() != null)
            view.get().onCategoriesSwap(draggedPosition, targetPosition);
    }
}
