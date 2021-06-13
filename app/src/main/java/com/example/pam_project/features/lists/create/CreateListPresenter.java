package com.example.pam_project.features.lists.create;

import com.example.pam_project.features.categories.list.CategoryInformation;
import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.example.pam_project.repositories.lists.ListsRepository;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CreateListPresenter {
    private final SchedulerProvider provider;
    private final CategoriesRepository categoriesRepository;
    private final ListsRepository listsRepository;
    private final WeakReference<CreateListView> view;
    private Disposable fetchListsDisposable;
    private Disposable insertListDisposable;

    public CreateListPresenter(final SchedulerProvider provider,
                               final CategoriesRepository categoriesRepository,
                               final ListsRepository listsRepository, final CreateListView view) {
        this.provider = provider;
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
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .subscribe(this::onCategoriesReceived, this::onCategoriesReceivedError);
    }

    private void onCategoriesReceived(final List<CategoryInformation> model) {
        if (view.get() != null) {
            view.get().bindCategories(model);
        }
    }

    private void onCategoriesReceivedError(final Throwable throwable) {
        // TODO
    }

    public void insertList(final String name, final Long categoryId) {
        insertListDisposable = listsRepository.insertList(name, categoryId)
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .subscribe(this::onListInserted, this::onListInsertedError);
    }

    private void onListInserted() {
        // TODO
    }

    private void onListInsertedError(Throwable throwable) {
        // TODO
    }

    public void onViewDetached() {
        if (fetchListsDisposable != null)
            fetchListsDisposable.dispose();
        if (insertListDisposable != null)
            insertListDisposable.dispose();
    }


}
