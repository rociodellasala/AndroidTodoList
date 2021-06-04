package com.example.pam_project.features.lists.create;

import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.example.pam_project.repositories.lists.ListsRepository;

import java.lang.ref.WeakReference;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CreateListPresenter {

    private final CategoriesRepository categoriesRepository;
    private final ListsRepository listsRepository;
    private final WeakReference<CreateListView> view;
    private final CompositeDisposable compositeDisposable;


    public CreateListPresenter(final CategoriesRepository categoriesRepository,
                               final ListsRepository listsRepository, final CreateListView view) {
        this.categoriesRepository = categoriesRepository;
        this.listsRepository = listsRepository;
        this.view = new WeakReference<>(view);
        this.compositeDisposable = new CompositeDisposable();
    }

    public void onViewAttached() {
        if (view.get() != null)
            fetchCategories();
    }


    private void fetchCategories() {
        Disposable disposable = categoriesRepository.getCategories()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if (view.get() != null) {
                        view.get().bindCategories(model);
                    }
                });

        compositeDisposable.add(disposable);
    }

    public void insertList(final String name, final Long categoryId) {
        Disposable disposable = Completable.fromAction(() -> {
            listsRepository.insertList(name, categoryId);
            if (view.get() != null) {
                view.get().onSuccessfulInsert();
            }
        }).onErrorComplete()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();

        compositeDisposable.add(disposable);
    }

    public void onViewDetached() {
        compositeDisposable.dispose();
    }


}
