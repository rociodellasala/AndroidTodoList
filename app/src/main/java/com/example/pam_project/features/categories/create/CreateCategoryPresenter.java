package com.example.pam_project.features.categories.create;

import com.example.pam_project.repositories.categories.CategoriesRepository;

import java.lang.ref.WeakReference;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CreateCategoryPresenter {

    private final CategoriesRepository repository;
    private final WeakReference<CreateCategoryView> view;
    private Disposable insertCategoryDisposable;

    public CreateCategoryPresenter(final CategoriesRepository repository,
                                   final CreateCategoryView view) {

        this.repository = repository;
        this.view = new WeakReference<>(view);
    }

    public void insertCategory(final String name, final String color) {
        insertCategoryDisposable = Completable.fromAction(() -> {
            final long id = repository.insertCategory(name, color);
            if (view.get() != null) {
                view.get().onSuccessfulInsert(id, name, color);
            }
        }).onErrorComplete()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void onViewDetached() {
        if (insertCategoryDisposable != null)
            insertCategoryDisposable.dispose();
    }

}
