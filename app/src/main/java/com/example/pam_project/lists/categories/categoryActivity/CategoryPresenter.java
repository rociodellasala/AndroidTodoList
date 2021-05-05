package com.example.pam_project.lists.categories.categoryActivity;

import com.example.pam_project.db.repositories.CategoriesRepository;

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
}
