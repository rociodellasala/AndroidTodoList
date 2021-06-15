package com.example.pam_project.networking.authors;

import java.util.List;

import io.reactivex.Single;

public interface AuthorsRepository {

    Single<List<AuthorsModel>> getAuthors();
}
