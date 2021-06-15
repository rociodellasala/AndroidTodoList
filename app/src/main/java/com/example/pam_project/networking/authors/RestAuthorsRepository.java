package com.example.pam_project.networking.authors;

import com.example.pam_project.networking.APIService;
import com.example.pam_project.networking.RetrofitUtils;

import java.util.List;

import io.reactivex.Single;

public class RestAuthorsRepository implements AuthorsRepository {
    private final APIService service;
    private final AuthorsMapper authorsMapper;

    public RestAuthorsRepository(final APIService service, final AuthorsMapper configMapper) {
        this.service = service;
        this.authorsMapper = configMapper;
    }

    @Override
    public Single<List<AuthorsModel>> getAuthors() {
        return RetrofitUtils.performRequest(service.getAuthors()).map(authorsMapper::map);
    }
}
