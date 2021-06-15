package com.example.pam_project.networking;

import io.reactivex.Single;
import retrofit2.Response;

public final class RetrofitUtils {
    public static <T> Single<T> performRequest(final Single<Response<T>> request) {
        return request.onErrorResumeNext(Single::error)
                .map(RetrofitUtils::unwrapResponse);
    }

    public static <T> T unwrapResponse(final Response<T> response) {
        if (RetrofitUtils.isRequestFailed(response)) {
            throw new IllegalArgumentException();
        }

        return response.body();
    }

    private static boolean isRequestFailed(final Response response) {
        return !response.isSuccessful() || isErrorCode(response.code());
    }

    private static boolean isErrorCode(final int statusCode) {
        return statusCode >= 400 && statusCode < 600;
    }
}
