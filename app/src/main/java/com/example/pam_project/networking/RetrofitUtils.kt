package com.example.pam_project.networking

import io.reactivex.Single
import retrofit2.Response

object RetrofitUtils {
    fun <T> performRequest(request: Single<Response<T>?>?): Single<T> {
        return request!!.onErrorResumeNext { exception: Throwable? -> Single.error(exception) }
                .map { obj: Response<T>? -> unwrapResponse() }
    }

    fun <T> unwrapResponse(response: Response<T>): T? {
        require(!isRequestFailed(response))
        return response.body()
    }

    private fun <T> isRequestFailed(response: Response<T>): Boolean {
        return !response.isSuccessful || isErrorCode(response.code())
    }

    private fun isErrorCode(statusCode: Int): Boolean {
        return statusCode >= 400 && statusCode < 600
    }
}