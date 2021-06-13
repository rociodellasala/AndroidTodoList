package com.example.pam_project.database.utils;

import android.app.Application;
import android.content.Context;

import com.example.pam_project.R;
import com.example.pam_project.database.categories.CategoryEntity;
import com.example.pam_project.di.ApplicationContainer;
import com.example.pam_project.di.ApplicationContainerLocator;
import com.example.pam_project.landing.FtuStorage;
import com.example.pam_project.utils.constants.AppColor;
import com.example.pam_project.utils.schedulers.SchedulerProvider;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Completable;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        final ApplicationContainer container = ApplicationContainerLocator.locateComponent(this);
        final SchedulerProvider schedulerProvider = container.getSchedulerProvider();
        final FtuStorage ftuStorage = container.getFtuStorage();

        if (ftuStorage.isActive()) {
            Context appContext = getApplicationContext();
            AppDatabase db = AppDatabase.getInstance(appContext);
            Completable.fromAction(
                    () -> db.categoryDao().insertCategory(createDefaultCategory(appContext))
            ).onErrorComplete()
             .subscribeOn(schedulerProvider.io())
             .observeOn(schedulerProvider.ui())
             .subscribe();
        }
    }

    private CategoryEntity createDefaultCategory(Context context) {
        String defaultCategory = context.getResources().getString(R.string.default_category);
        final List<AppColor> colors = Arrays.asList(AppColor.values());
        return new CategoryEntity(defaultCategory, colors.get(0).toString());
    }
}
