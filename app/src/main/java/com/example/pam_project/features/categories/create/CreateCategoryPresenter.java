package com.example.pam_project.features.categories.create;

import com.example.pam_project.repositories.categories.CategoriesRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CreateCategoryPresenter {
    private final CategoriesRepository repository;
    private Disposable insertCategoryDisposable;

    public CreateCategoryPresenter(final CategoriesRepository repository) {
        this.repository = repository;
    }

    public void insertCategory(final String name, final String color) {
        insertCategoryDisposable = repository.insertCategory(name, color)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCategoryInserted, this::onCategoryInsertedError);
    }

    private void onCategoryInserted() {
        // TODO
    }

    private void onCategoryInsertedError(final Throwable throwable) {
        // TODO
    }

    public void onViewDetached() {
        if (insertCategoryDisposable != null)
            insertCategoryDisposable.dispose();
    }

}
