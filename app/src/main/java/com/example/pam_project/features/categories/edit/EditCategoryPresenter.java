
package com.example.pam_project.features.categories.edit;

import com.example.pam_project.features.categories.list.CategoryInformation;
import com.example.pam_project.repositories.categories.CategoriesRepository;

import java.lang.ref.WeakReference;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EditCategoryPresenter {

    private final long categoryId;
    private final CategoriesRepository repository;
    private final WeakReference<EditCategoryView> view;
    private Disposable editCategoryDisposable;
    private Disposable deleteCategoryDisposable;

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
        editCategoryDisposable = Completable.fromAction(() -> {
            repository.updateCategory(categoryId, name, color);
            if (view.get() != null) {
                view.get().onCategoryEdit();
            }
        }).onErrorComplete()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void deleteCategory(final long id) {
        deleteCategoryDisposable = Completable.fromAction(() -> {
            repository.deleteCategory(id);
            if (view.get() != null) {
                view.get().onCategoryDelete();
            }
        }).onErrorComplete()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void onViewDetached() {
        if (editCategoryDisposable != null)
            editCategoryDisposable.dispose();
        if (deleteCategoryDisposable != null)
            deleteCategoryDisposable.dispose();
    }

}