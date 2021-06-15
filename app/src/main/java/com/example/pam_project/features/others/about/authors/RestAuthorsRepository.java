package com.example.pam_project.features.others.about.authors;

import com.example.pam_project.features.others.about.APIService;
import com.example.pam_project.utils.networking.RetrofitUtils;

import io.reactivex.Single;

public class RestAuthorsRepository implements AuthorsRepository {
    private final APIService service;
    private final AuthorsMapper authorsMapper;

    public RestAuthorsRepository(final APIService service, final AuthorsMapper configMapper) {
        this.service = service;
        this.authorsMapper = configMapper;
    }

    @Override
    public Single<AuthorsModel> getAuthors() {
        return RetrofitUtils.performRequest(service.getAuthors()).map(authorsMapper::map);
    }
}
