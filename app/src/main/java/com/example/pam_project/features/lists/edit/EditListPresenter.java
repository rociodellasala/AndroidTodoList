package com.example.pam_project.features.lists.edit;

import com.example.pam_project.features.lists.list.ListInformation;
import com.example.pam_project.repositories.categories.CategoriesRepository;
import com.example.pam_project.repositories.lists.ListsRepository;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.Disposable;

public class EditListPresenter {
    private final SchedulerProvider provider;
    private final CategoriesRepository categoriesRepository;
    private final ListsRepository listsRepository;
    private final WeakReference<EditListView> view;
    private Disposable fetchCategoriesDisposable;
    private Disposable updateListDisposable;
    private Disposable deleteListDisposable;

    public EditListPresenter(final SchedulerProvider provider,
                             final CategoriesRepository categoriesRepository,
                             final ListsRepository listsRepository,
                             final EditListView view) {
        this.provider = provider;
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
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
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

    public void updateList(final long id, final String name, final Long categoryId) {
        updateListDisposable = listsRepository.updateList(id, name, categoryId)
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .subscribe(this::onListUpdated, this::onListUpdatedError);
    }

    private void onListUpdated() {
        // TODO
    }

    private void onListUpdatedError(final Throwable throwable) {
        // TODO
    }

    public void deleteList(final long id){
        deleteListDisposable = listsRepository.deleteList(id)
                .subscribeOn(provider.computation())
                .observeOn(provider.ui())
                .subscribe(this::onListDeleted, this::onListDeletedError);
    }

    private void onListDeleted() {
        if (view.get() != null) {
            view.get().onListDelete();
        }
    }

    private void onListDeletedError(final Throwable throwable) {
        // TODO
    }

    public void onViewDetached() {
        if(fetchCategoriesDisposable != null)
            fetchCategoriesDisposable.dispose();
        if(updateListDisposable != null)
            updateListDisposable.dispose();
        if(deleteListDisposable != null)
            deleteListDisposable.dispose();
    }

}
