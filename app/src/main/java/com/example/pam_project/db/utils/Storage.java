package com.example.pam_project.db.utils;

public interface Storage {

    void setUpStorage();

    AppDatabase getStorage();

    void populateStorage();

    void clearStorage();
}
