package com.example.pam_project.features.lists.list;

import com.example.pam_project.features.categories.list.CategoryInformation;
import com.example.pam_project.landing.FtuStorage;
import com.example.pam_project.repositories.categories.CategoriesRepository;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ListPresenter {
    private final FtuStorage ftuStorage;
    private final WeakReference<ListView> view;
    private final CategoriesRepository categoriesRepository;
    private Disposable fetchListsDisposable;
    private Disposable fetchCategoriesDisposable;

    public ListPresenter(final FtuStorage ftuStorage, final CategoriesRepository categoriesRepository,
                         final ListView view) {
        this.ftuStorage = ftuStorage;
        this.categoriesRepository = categoriesRepository;
        this.view = new WeakReference<>(view);
    }

    public void onViewAttached() {
        if (ftuStorage.isActive()) {
            ftuStorage.deactivate();

            if (view.get() != null)
                view.get().launchFtu();
        } else {
            if (view.get() != null) {
                fetchCategories();
                fetchLists();
            }
        }
    }

    private void fetchCategories() {
        fetchCategoriesDisposable = categoriesRepository.getCategories()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCategoriesReceived, this::onCategoriesReceivedError);
    }

    private void onCategoriesReceived(List<CategoryInformation> model) {
        if (view.get() != null) {
            List<CategoryInformation> finalList = new ArrayList<>(model);
            Collections.sort(finalList);
            view.get().bindCategories(finalList);
        }
    }

    private void onCategoriesReceivedError(final Throwable e) {
        if (view.get() != null) {
            // view.get().onCategoriesReceivedError();
        }
    }

    private void fetchLists() {
        fetchListsDisposable = categoriesRepository.getCategoriesWithLists()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onListsReceived, this::onListsReceivedError);
    }

    private void onListsReceived(Map<CategoryInformation, List<ListInformation>> model) {
        if (view.get() != null) {
            List<ListInformation> finalList = new ArrayList<>();
            for (final List<ListInformation> list : model.values()) {
                finalList.addAll(list);
            }
            Collections.sort(finalList);
            view.get().bindLists(finalList);
        }
    }

    private void onListsReceivedError(final Throwable e) {
        if (view.get() != null) {
            // view.get().onListsReceivedError();
        }
    }

    public void performSearch(String searchQuery){
        if (view.get() != null)
            view.get().bindSearchedLists(searchQuery);
    }

    public void onSearchDetached(){
        if (view.get() != null)
            view.get().unbindSearchedLists();
    }

    public void onListClicked(final long id) {
        if (view.get() != null)
            view.get().showListContent(id);
    }

    public void onButtonClicked() {
        if (view.get() != null)
            view.get().showAddList();
    }

    public void onFilterDialog() {
        if (view.get() != null)
            view.get().showFilterDialog();
    }

    public void onSortByDialog() {
        if (view.get() != null)
            view.get().showSortByDialog();
    }

    public void onEmptyList(){
        if (view.get() != null)
            view.get().showEmptyMessage();
    }

    public void onManageCategories() {
        if (view.get() != null)
            view.get().showManageCategories();
    }

    public void onViewDetached() {
        if (fetchListsDisposable != null)
            fetchListsDisposable.dispose();
        if (fetchCategoriesDisposable != null)
            fetchCategoriesDisposable.dispose();
    }
}
