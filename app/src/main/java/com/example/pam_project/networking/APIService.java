package com.example.pam_project.networking;

import com.example.pam_project.networking.authors.ListAuthorsResponse;
import com.example.pam_project.networking.version.VersionResponse;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;

public interface APIService {

    @GET("version")
    Single<Response<VersionResponse>> getVersion();

    @GET("authors")
    Single<Response<ListAuthorsResponse>> getAuthors();
}
