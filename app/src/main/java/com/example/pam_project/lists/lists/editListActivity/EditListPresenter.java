package com.example.pam_project.lists.lists.editListActivity;

import com.example.pam_project.db.repositories.CategoriesRepository;
import com.example.pam_project.db.repositories.ListsRepository;
import com.example.pam_project.lists.lists.components.ListInformation;

import java.lang.ref.WeakReference;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EditListPresenter {

    private final CategoriesRepository categoriesRepository;
    private final ListsRepository listsRepository;
    private final WeakReference<EditListView> view;
    private Disposable disposable;


    public EditListPresenter(final CategoriesRepository categoriesRepository, final ListsRepository listsRepository, final EditListView view) {
        this.categoriesRepository = categoriesRepository;
        this.listsRepository = listsRepository;
        this.view = new WeakReference<>(view);
    }

    public void onViewAttached(final long id) {
        if(view.get() != null){
            fetchCategories(id);
        }

    }


    private void fetchCategories(final long id){
        disposable = categoriesRepository.getCategories()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if(view.get() != null){
                        view.get().bindCategories(model);
                        fetchList(id);
                    }
                });
    }

    private void fetchList(final long id){
        ListInformation model = listsRepository.getList(id);
        if(view.get() != null)
            view.get().bindList(model);
    }

    public void editList(final long id, final String name, final Long categoryId){
        Completable.fromAction(() -> {
            listsRepository.updateList(id, name, categoryId);
            if(view.get() != null){
                view.get().onSuccessfulUpdate(name, categoryId);
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
