package com.example.pam_project.utils.networking;

import java.io.IOException;
import java.net.UnknownHostException;

import io.reactivex.Single;
import retrofit2.Response;

public final class RetrofitUtils {
    public static <T> Single<T> performRequest(final Single<Response<T>> request) {
        return request.onErrorResumeNext(t->Single.error(RetrofitUtils.convertException(t)))
                .map(RetrofitUtils::unwrapResponse);
    }

    public static <T> T unwrapResponse(final Response<T> response) {
        if (RetrofitUtils.isRequestFailed(response)) {
            throw new IllegalArgumentException();
        }

        return response.body();
    }

    public static Throwable convertException(final Throwable throwable) {
        if (throwable instanceof UnknownHostException) {
           // return new NoConnectionPendingException(throwable);
        }

        if (throwable instanceof IOException) {
            //return new RequestExceptions(throwable);
        }
        return throwable;
    }

    private static boolean isRequestFailed(final Response response) {
        return !response.isSuccessful() || isErrorCode(response.code());
    }

    private static boolean isErrorCode(final int statusCode) {
        return statusCode >= 400 && statusCode < 600;
    }
}
