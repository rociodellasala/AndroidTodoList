package com.example.pam_project.features.categories.create;

import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.Disposable;

public class CreateCategoryPresenter {
    private final SchedulerProvider provider;
    private final CategoriesRepository repository;
    private final WeakReference<CreateCategoryView> view;
    private Disposable insertCategoryDisposable;

    public CreateCategoryPresenter(final SchedulerProvider provider, final CategoriesRepository repository, final CreateCategoryView view) {
        this.provider = provider;
        this.repository = repository;
        this.view = new WeakReference<>(view);
    }

    public void insertCategory(final String name, final String color) {
        insertCategoryDisposable = repository.insertCategory(name, color)
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .subscribe(null, this::onCategoryInsertedError);
    }

    private void onCategoryInsertedError(final Throwable throwable) {
        if (view.get() != null) {
            view.get().onCategoryInsertedError();
        }
    }

    public void onViewDetached() {
        if (insertCategoryDisposable != null)
            insertCategoryDisposable.dispose();
    }

}
