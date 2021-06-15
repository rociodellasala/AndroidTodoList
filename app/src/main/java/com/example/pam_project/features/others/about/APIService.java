package com.example.pam_project.features.others.about;

import com.example.pam_project.features.others.about.authors.AuthorsResponse;
import com.example.pam_project.features.others.about.version.VersionResponse;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;

public interface APIService {

    @GET("version")
    Single<Response<VersionResponse>> getVersion();

    @GET("authors")
    Single<Response<AuthorsResponse>> getAuthors();
}
