package com.example.pam_project.db.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.pam_project.R;
import com.example.pam_project.db.entities.CategoryEntity;
import com.example.pam_project.landing.FtuStorage;
import com.example.pam_project.landing.SharedPreferencesFtuStorage;
import com.example.pam_project.utils.AppColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainApplication extends Application {
    private static final String PAM_PREF = "app-pref";

    @Override
    public void onCreate() {
        super.onCreate();

        final SharedPreferences sharedPref = getSharedPreferences(PAM_PREF, MODE_PRIVATE);
        final FtuStorage ftuStorage = new SharedPreferencesFtuStorage(sharedPref);

        if (ftuStorage.isActive()) {
            Completable.fromAction(() -> {
                Context appContext = getApplicationContext();
                AppDatabase db = AppDatabase.getInstance(appContext);
                db.categoryDao().insertAllCategories(createDefaultCategory(appContext));
            }).onErrorComplete().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe();
        }
    }

    private List<CategoryEntity> createDefaultCategory(Context context) {
        List<CategoryEntity> listOfCategories = new ArrayList<>();
        String defaultCategory = context.getResources().getString(R.string.default_category);
        final List<AppColor> colors = Arrays.asList(AppColor.values());

        CategoryEntity category = new CategoryEntity(defaultCategory, colors.get(0).toString());
        listOfCategories.add(category);

        return listOfCategories;
    }
}
