package com.example.pam_project.features.lists.edit;

import com.example.pam_project.features.lists.list.ListInformation;
import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.example.pam_project.repositories.lists.ListsRepository;

import java.lang.ref.WeakReference;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EditListPresenter {

    private final CategoriesRepository categoriesRepository;
    private final ListsRepository listsRepository;
    private final WeakReference<EditListView> view;
    private Disposable fetchCategoriesDisposable;
    private Disposable editListDisposable;
    private Disposable deleteListDisposable;


    public EditListPresenter(final CategoriesRepository categoriesRepository,
                             final ListsRepository listsRepository, final EditListView view) {

        this.categoriesRepository = categoriesRepository;
        this.listsRepository = listsRepository;
        this.view = new WeakReference<>(view);
    }

    public void onViewAttached(final long id) {
        if (view.get() != null) {
            fetchCategories(id);
        }
    }

    private void fetchCategories(final long id) {
        fetchCategoriesDisposable = categoriesRepository.getCategories()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if (view.get() != null) {
                        view.get().bindCategories(model);
                        fetchList(id);
                    }
                }, this::onCategoriesReceivedError);
    }

    private void fetchList(final long id) {
        ListInformation model = listsRepository.getList(id);
        if (view.get() != null)
            view.get().bindList(model);
    }

    private void onCategoriesReceivedError(final Throwable throwable) {
        // TODO
    }

    public void editList(final long id, final String name, final Long categoryId) {
        editListDisposable = listsRepository.updateList(id, name, categoryId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onListUpdate, this::onListUpdateError);
    }

    private void onListUpdate() {
        // TODO
    }

    private void onListUpdateError(final Throwable throwable) {
        // TODO
    }

    public void deleteList(final long id){
        deleteListDisposable = listsRepository.deleteList(id)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onListDelete, this::onListDeleteError);
    }

    private void onListDelete() {
        if (view.get() != null) {
            view.get().onListDelete();
        }
    }

    private void onListDeleteError(final Throwable throwable) {
        // TODO
    }

    public void onViewDetached() {
        if(fetchCategoriesDisposable != null)
            fetchCategoriesDisposable.dispose();
        if(editListDisposable != null)
            editListDisposable.dispose();
        if(deleteListDisposable != null)
            deleteListDisposable.dispose();
    }

}
