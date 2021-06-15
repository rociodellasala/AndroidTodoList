package com.example.pam_project.features.others.about.authors;

import io.reactivex.Single;

public interface AuthorsRepository {

    Single<AuthorsModel> getAuthors();
}
