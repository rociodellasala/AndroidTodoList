package com.example.pam_project.networking

import com.example.pam_project.networking.authors.ListAuthorsResponse
import com.example.pam_project.networking.version.VersionResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface APIService {
    @get:GET("version")
    val version: Single<Response<VersionResponse?>?>?

    @get:GET("authors")
    val authors: Single<Response<ListAuthorsResponse?>?>?
}