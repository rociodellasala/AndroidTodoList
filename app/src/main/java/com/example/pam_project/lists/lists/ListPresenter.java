package com.example.pam_project.lists.lists;

import com.example.pam_project.db.entities.ListEntity;
import com.example.pam_project.db.relationships.CategoriesWithLists;
import com.example.pam_project.landing.FtuStorage;
import com.example.pam_project.utils.AppColor;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ListPresenter {

    private final FtuStorage ftuStorage;
    private final WeakReference<ListView> view;
    private final CategoriesRepository repository;
    private Disposable disposable;

    public ListPresenter(final FtuStorage ftuStorage, final CategoriesRepository repository, final ListView view){
        this.ftuStorage = ftuStorage;
        this.repository = repository;
        this.view = new WeakReference<>(view);
    }

    public void onViewAttached() {
        if (ftuStorage.isActive()) {
            ftuStorage.deactivate();

            if(view.get() != null)
                view.get().launchFtu();
        }else{
            view.get().showLists();
            fetchLists();
        }
    }

    private void fetchLists(){
        disposable = repository.getCategoriesWithLists()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if(view.get() != null){
                        view.get().bindLists(adaptModel(model));
                    }
                });
    }

    private List<ListInformation> adaptModel(List<CategoriesWithLists> model) {
        final List<ListInformation> list = new ArrayList<>();

        for (final CategoriesWithLists entity : model) {
            for (final ListEntity listEntity : entity.lists) {
                AppColor color = findColor(entity.category.color);
                list.add(new ListInformation(listEntity.id, listEntity.name, color));
            }
        }

        return list;
    }

    private static AppColor findColor(String color) {
        final List<AppColor> colors = Arrays.asList(AppColor.values());

        for (int i = 0; i < colors.size(); i++) {
            if (color.equals(colors.get(i).toString())) {
                return colors.get(i);
            }
        }

        return null;
    }

    public void onListClicked(final long id) {
        if(view.get() != null)
            view.get().showListContent(id);
    }

    public void onViewDetached() {
        disposable.dispose();
    }


}
