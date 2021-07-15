package com.example.pam_project.networking

import com.example.pam_project.networking.authors.ListAuthorsResponse
import com.example.pam_project.networking.version.VersionResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class APIServiceImplementation : APIService {
    private val retrofit: Retrofit
    override val authors: Single<Response<ListAuthorsResponse?>?>?
        get() {
            val service = retrofit.create(APIService::class.java)
            return service.authors
        }
    override val version: Single<Response<VersionResponse?>?>?
        get() {
            val service = retrofit.create(APIService::class.java)
            return service.version
        }

    companion object {
        private const val BASE_URL = "https://private-ad7668-rememberit.apiary-mock.com/"
    }

    init {
        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }
}