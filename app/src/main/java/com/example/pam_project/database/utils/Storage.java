package com.example.pam_project.database.utils;

public interface Storage {

    void setUpStorage();

    AppDatabase getStorage();

    void clearStorage();
}
