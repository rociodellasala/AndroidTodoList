package com.example.pam_project.networking.authors

import com.example.pam_project.networking.APIService
import com.example.pam_project.networking.RetrofitUtils
import io.reactivex.Single

class RestAuthorsRepository(private val service: APIService?, private val authorsMapper: AuthorsMapper?) : AuthorsRepository {
    override val authors: Single<List<AuthorsModel?>?>
        get() = RetrofitUtils.performRequest(service.getAuthors()).map { response: ListAuthorsResponse? -> authorsMapper!!.map(response) }
}