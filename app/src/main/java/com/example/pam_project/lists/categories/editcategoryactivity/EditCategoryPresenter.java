package com.example.pam_project.lists.categories.editcategoryactivity;

import com.example.pam_project.db.repositories.CategoriesRepository;
import com.example.pam_project.lists.categories.components.CategoryInformation;

import java.lang.ref.WeakReference;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EditCategoryPresenter {

    private final long categoryId;
    private final CategoriesRepository repository;
    private final WeakReference<EditCategoryView> view;
    private Disposable disposable;

    public EditCategoryPresenter(final long categoryId, final CategoriesRepository repository,
                                 final EditCategoryView view) {

        this.categoryId = categoryId;
        this.repository = repository;
        this.view = new WeakReference<>(view);
    }

    public void onViewAttached() {
        if (view.get() != null) {
            CategoryInformation model = repository.getCategory(categoryId);
            view.get().bindCategory(model);
        }
    }

    public void editCategory(final String name, final String color) {
        Completable.fromAction(() -> {
            repository.updateCategory(categoryId, name, color);
            if(view.get() != null){
                view.get().onSuccessfulUpdate(name, color);
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
