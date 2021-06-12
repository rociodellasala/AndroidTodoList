package com.example.pam_project.features.lists.create;

import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.example.pam_project.repositories.lists.ListsRepository;

import java.lang.ref.WeakReference;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CreateListPresenter {

    private final CategoriesRepository categoriesRepository;
    private final ListsRepository listsRepository;
    private final WeakReference<CreateListView> view;
    private Disposable fetchListsDisposable;
    private Disposable insertListDisposable;

    public CreateListPresenter(final CategoriesRepository categoriesRepository,
                               final ListsRepository listsRepository, final CreateListView view) {
        this.categoriesRepository = categoriesRepository;
        this.listsRepository = listsRepository;
        this.view = new WeakReference<>(view);
    }

    public void onViewAttached() {
        if (view.get() != null)
            fetchCategories();
    }


    private void fetchCategories() {
        fetchListsDisposable = categoriesRepository.getCategories()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if (view.get() != null) {
                        view.get().bindCategories(model);
                    }
                });
    }

    public void insertList(final String name, final Long categoryId) {
        insertListDisposable = Completable.fromAction(() -> {
            listsRepository.insertList(name, categoryId);
            if (view.get() != null) {
                view.get().onSuccessfulInsert();
            }
        }).onErrorComplete()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void onViewDetached() {
        if (fetchListsDisposable != null)
            fetchListsDisposable.dispose();
        if (insertListDisposable != null)
            insertListDisposable.dispose();
    }


}
