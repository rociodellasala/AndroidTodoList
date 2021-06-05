package com.example.pam_project.lists.lists.listActivity;

import com.example.pam_project.db.repositories.CategoriesRepository;
import com.example.pam_project.db.repositories.ListsRepository;
import com.example.pam_project.landing.FtuStorage;
import com.example.pam_project.lists.categories.components.CategoryInformation;
import com.example.pam_project.lists.lists.components.ListInformation;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ListPresenter {

    private final FtuStorage ftuStorage;
    private final WeakReference<ListView> view;
    private final CategoriesRepository categoriesRepository;
    private final ListsRepository listsRepository;
    private final CompositeDisposable compositeDisposable;

    public ListPresenter(final FtuStorage ftuStorage, final CategoriesRepository categoriesRepository,
                         final ListsRepository listsRepository, final ListView view) {
        this.ftuStorage = ftuStorage;
        this.categoriesRepository = categoriesRepository;
        this.listsRepository = listsRepository;
        this.view = new WeakReference<>(view);
        this.compositeDisposable = new CompositeDisposable();
    }

    public void onViewAttached() {
        if (ftuStorage.isActive()) {
            ftuStorage.deactivate();

            if (view.get() != null)
                view.get().launchFtu();
        } else {
            if (view.get() != null) {
                view.get().showLists();
                fetchLists();
            }
        }
    }

    private void fetchLists() {
        fetchCategories();

        Disposable disposable = categoriesRepository.getCategoriesWithLists()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if (view.get() != null) {
                        List<ListInformation> finalList = new ArrayList<>();
                        for (final List<ListInformation> list : model.values()) {
                            finalList.addAll(list);
                        }
                        Collections.sort(finalList);
                        view.get().bindLists(finalList);
                    }
                });

        compositeDisposable.add(disposable);
    }

    private void fetchCategories() {
        Disposable disposable = categoriesRepository.getCategories()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if (view.get() != null) {
                        List<CategoryInformation> finalList = new ArrayList<>(model);
                        Collections.sort(finalList);
                        view.get().bindCategories(finalList);
                    }
                });

        compositeDisposable.add(disposable);
    }

    public void appendList(final long id) {
        ListInformation model = listsRepository.getList(id);
        if (view.get() != null)
            view.get().bindList(model);
    }

    public void onListClicked(final long id) {
        if (view.get() != null)
            view.get().showListContent(id);
    }

    public void onButtonClicked() {
        if (view.get() != null)
            view.get().showAddList();
    }

    public void onSearchBar() {
        if (view.get() != null)
            view.get().showSearchBar();
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
        compositeDisposable.dispose();
    }
}
