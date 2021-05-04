package com.example.pam_project.lists.categories.createcategoryactivity;

import com.example.pam_project.db.repositories.CategoriesRepository;

import java.lang.ref.WeakReference;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CreateCategoryPresenter {

    private final CategoriesRepository repository;
    private final WeakReference<CreateCategoryView> view;
    private Disposable disposable;

    public CreateCategoryPresenter(final CategoriesRepository repository,
                                   final CreateCategoryView view) {

        this.repository = repository;
        this.view = new WeakReference<>(view);
    }

    public void onViewAttached() {
    }

    public void insertCategory(final String name, final String color) {
        Completable.fromAction(() -> {
            final long id = repository.insertCategory(name, color);
            if(view.get() != null) {
                view.get().onSuccessfulInsert(id, name, color);
            }
        }).onErrorComplete()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void onViewDetached() {
        if(disposable != null)
            disposable.dispose();
    }

}
