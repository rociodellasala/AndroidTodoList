package com.example.pam_project.features.categories.create;

import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

import io.reactivex.disposables.Disposable;

public class CreateCategoryPresenter {
    private final SchedulerProvider provider;
    private final CategoriesRepository repository;
    private Disposable insertCategoryDisposable;

    public CreateCategoryPresenter(final SchedulerProvider provider,
                                   final CategoriesRepository repository) {
        this.provider = provider;
        this.repository = repository;
    }

    public void insertCategory(final String name, final String color) {
        insertCategoryDisposable = repository.insertCategory(name, color)
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
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
