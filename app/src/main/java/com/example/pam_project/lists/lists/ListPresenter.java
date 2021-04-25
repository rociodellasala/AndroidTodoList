package com.example.pam_project.lists.lists;

import com.example.pam_project.landing.WelcomeActivity;

import java.lang.ref.WeakReference;

public class ListPresenter {

    private final FtuStorage ftuStorage;
    private final WeakReference<ListView> view;

    public ListPresenter(final FtuStorage ftuStorage, final ListView view){
        this.ftuStorage = ftuStorage;
        this.view = new WeakReference<>(view);
    }

    public void onViewAttached() {
        if (ftuStorage.isActive()) {
            ftuStorage.deactivate();

            if(view.get() != null)
                view.get().launchFtu();
        }
    }

    public void onViewDetached() {
    }
}
