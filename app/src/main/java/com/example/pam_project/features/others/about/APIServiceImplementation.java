package com.example.pam_project.features.others.about;

import com.example.pam_project.features.others.about.authors.AuthorsResponse;
import com.example.pam_project.features.others.about.version.VersionResponse;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIServiceImplementation implements APIService {

    private static final String BASE_URL = "https://private-ad7668-rememberit.apiary-mock.com/";

    private Retrofit retrofit;

    public APIServiceImplementation() {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
    }

    @Override
    public Single<Response<AuthorsResponse>> getAuthors() {
        APIService service = retrofit.create(APIService.class);
        return service.getAuthors();
    }

    @Override
    public Single<Response<VersionResponse>> getVersion() {
        APIService service = retrofit.create(APIService.class);
        return service.getVersion();
    }
}
