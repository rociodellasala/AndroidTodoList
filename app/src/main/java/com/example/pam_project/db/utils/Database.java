package com.example.pam_project.db.utils;

import android.content.Context;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Database implements Storage {

    private final Context context;
    private AppDatabase database;

    public Database(Context context) {
        this.context = context;
    }

    @Override
    public void setUpStorage() {
        this.database = AppDatabase.getInstance(context);
    }

    @Override
    public AppDatabase getStorage() {
        return database;
    }

    @Override
    public void populateStorage() {
        DatabaseHelper helper = new DatabaseHelper();
        helper.createDB(context);
    }

    @Override
    public void clearStorage() {
        Completable.fromAction(AppDatabase::nukeDatabase
        ).onErrorComplete().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe();
    }
}
